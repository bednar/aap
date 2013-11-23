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
        Integer position = processPosition(klass);

        String path = processPath(klass);

        String[] consumes = processConsumes(klass);
        String[] produces = processProduces(klass);

        String shortDescription  = processShortDescription(klass);
        String description       = processDescription(klass);

        List<OperationModel> operations = processOperations(klass, path, consumes, produces);

        ApiModel model = new ApiModel();

        model
                .setPosition(position)
                .setType(klass)
                .setPath(path)
                .setConsumes(consumes)
                .setProduces(produces)
                .setShortDescription(shortDescription)
                .setDescription(description)
                .setOperations(operations);

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

    private int processPosition(final @Nonnull Class<?> klass)
    {
        Api api = klass.getAnnotation(Api.class);

        return api != null ? api.position() : 0;
    }

    @Nonnull
    private String processShortDescription(final @Nonnull Class<?> klass)
    {
        Api api = klass.getAnnotation(Api.class);

        return api != null ? api.value() : "";
    }

    @Nonnull
    private String processDescription(final @Nonnull Class<?> klass)
    {
        Api api = klass.getAnnotation(Api.class);

        return api != null ? api.description() : "";
    }

    @Nonnull
    private List<OperationModel> processOperations(@Nonnull final Class<?> klass,
                                                   @Nonnull final String parentPath,
                                                   @Nonnull final String[] consumes,
                                                   @Nonnull final String[] produces)
    {
        List<Method> methods = Lists.newArrayList(klass.getDeclaredMethods());

        return FluentIterable
                .from(methods)
                .filter(
                        new Predicate<Method>()
                        {
                            @Override
                            public boolean apply(final @Nullable Method method)
                            {
                                return method != null && method.getAnnotation(ApiOperation.class) != null;
                            }
                        })
                .transform(new OperationModelTransform(parentPath, consumes, produces))
                .toSortedList(
                        new Comparator<OperationModel>()
                        {
                            @Override
                            public int compare(final OperationModel model1, final OperationModel model2)
                            {
                                return ComparisonChain.start()
                                        .compare(model1.getPosition(), model2.getPosition())
                                        .result();
                            }
                        });
    }
}
