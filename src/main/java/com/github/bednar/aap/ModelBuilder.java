package com.github.bednar.aap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.github.bednar.aap.model.api.ApiModel;
import com.github.bednar.aap.model.api.OperationModel;
import com.github.bednar.aap.model.api.OperationModelTransform;
import com.github.bednar.aap.model.entity.EntityModel;
import com.github.bednar.aap.model.entity.EntityModelTransformer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author Jakub Bednář (06/10/2013 10:42 AM)
 */
public final class ModelBuilder
{
    private final Map<Class, ApiModel> apiCache = Maps.newConcurrentMap();
    private final Map<Class, EntityModel> entityCache = Maps.newConcurrentMap();

    private ModelBuilder()
    {
    }

    private final static ModelBuilder intance = new ModelBuilder();

    @Nonnull
    public static ModelBuilder getInstance()
    {
        return intance;
    }

    @Nonnull
    public ApiModel getApiModel(final @Nonnull Class klass)
    {
        ApiModel model = apiCache.get(klass);
        if (model == null)
        {
            return buildApiModel(klass);
        }

        return model;
    }

    @Nonnull
    public EntityModel getEntityModel(final @Nonnull Class klass)
    {
        EntityModel model = entityCache.get(klass);
        if (model == null)
        {
            return buildEntityModel(klass);
        }

        return model;
    }

    @Nonnull
    private synchronized ApiModel buildApiModel(final @Nonnull Class<?> klass)
    {
        ApiModel model = apiCache.get(klass);
        if (model == null)
        {
            model = new ApiModelTransform().apply(klass);

            apiCache.put(klass, model);
        }

        return model;
    }

    //TODO jb buildery do samostatnych trid
    @Nonnull
    private synchronized EntityModel buildEntityModel(final @Nonnull Class klass)
    {
        EntityModel model = entityCache.get(klass);
        if (model == null)
        {
            model = new EntityModelTransformer().apply(klass);

            entityCache.put(klass, model);
        }

        return model;
    }

    private class ApiModelTransform implements Function<Class, ApiModel>
    {
        @Nonnull
        @Override
        public ApiModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Class klass)
        {
            ApiModel model = new ApiModel();

            model.path = processPath(klass);

            model.consumes = processConsumes(klass);
            model.produces = processProduces(klass);

            model.shortDescription = processShortDescription(klass);

            model.operations = processOperations(klass, model);

            return model;
        }
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
