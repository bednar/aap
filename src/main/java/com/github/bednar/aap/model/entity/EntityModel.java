package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jakub Bednář (06/10/2013 2:21 PM)
 */
public final class EntityModel
{
    private TypeModel type = new TypeModel(Void.class.getCanonicalName());

    private String shortDescription = "";

    private List<PropertyModel> properties = Lists.newArrayList();

    @Nonnull
    public TypeModel getType()
    {
        return type;
    }

    @Nonnull
    public EntityModel setType(final @Nonnull TypeModel type)
    {
        this.type = type;

        return this;
    }

    @Nonnull
    public String getShortDescription()
    {
        return shortDescription;
    }

    @Nonnull
    public EntityModel setShortDescription(final @Nonnull String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }

    @Nonnull
    public List<PropertyModel> getProperties()
    {
        return properties;
    }

    @Nonnull
    public EntityModel setProperties(final @Nonnull List<PropertyModel> properties)
    {
        this.properties = properties;

        return this;
    }
}
