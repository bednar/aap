package com.github.bednar.aap.model;

import javax.annotation.Nonnull;
import java.util.Map;

import com.github.bednar.aap.model.api.ApiModel;
import com.github.bednar.aap.model.api.ApiModelTransform;
import com.github.bednar.aap.model.entity.EntityModel;
import com.github.bednar.aap.model.entity.EntityModelClassTransformer;
import com.google.common.collect.Maps;

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
    public EntityModel getEntityModelClass(final @Nonnull Class klass)
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

    @Nonnull
    private synchronized EntityModel buildEntityModel(final @Nonnull Class klass)
    {
        EntityModel model = entityCache.get(klass);
        if (model == null)
        {
            model = new EntityModelClassTransformer().apply(klass);

            entityCache.put(klass, model);
        }

        return model;
    }
}
