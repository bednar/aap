package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
* @author Jakub Bednář (06/10/2013 6:15 PM)
*/
public class ApiModelTransform implements Function<Class, ApiModel>
{
    @Nonnull
    @Override
    public ApiModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Class klass)
    {
        ApiModel model = new ApiModel();

        model.type = klass;

        model.path = processPath(klass);

        model.consumes = processConsumes(klass);
        model.produces = processProduces(klass);

        model.shortDescription = processShortDescription(klass);

        model.operations = processOperations(klass, model);

        return model;
    }

    @Nonnull
    private String processPath(final @Nonnull Class<?> klass)
    {
        Path path = klass.getAnnotation(Path.class);

        return path != null ? path.value() : "";
    }

    @Nonnull
    private String[] processConsumes(final @Nonnull Class<?> klass)
    {
        Consumes consumes = klass.getAnnotation(Consumes.class);

        return consumes != null ? consumes.value() : new String[]{};
    }

    @Nonnull
    private String[] processProduces(final @Nonnull Class<?> klass)
    {
        Produces produces = klass.getAnnotation(Produces.class);

        return produces != null ? produces.value() : new String[]{};
    }

    @Nonnull
    private String processShortDescription(final @Nonnull Class<?> klass)
    {
        Api api = klass.getAnnotation(Api.class);

        return api != null ? api.value() : "";
    }

    @Nonnull
    private List<OperationModel> processOperations(final @Nonnull Class<?> klass, final ApiModel model)
    {
        List<Method> methods = Lists.newArrayList(klass.getDeclaredMethods());

        return FluentIterable
                .from(methods)
                .filter(
                        new Predicate<Method>()
                        {
                            @Override
                            public boolean apply(@Nullable final Method method)
                            {
                                return method != null && method.getAnnotation(ApiOperation.class) != null;
                            }

                        })
                .transform(new OperationModelTransform(model))
                .toSortedList(
                        new Comparator<OperationModel>()
                        {
                            @Override
                            public int compare(final OperationModel operation1, final OperationModel operation2)
                            {
                                return ComparisonChain.start().compare(operation1.position, operation2.position).result();
                            }

                        });
    }
}
