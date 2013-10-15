package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;

/**
 * @author Jakub Bednář (06/10/2013 1:23 PM)
 */
public final class ParameterModel
{
    private String name = "";
    private String shortDescription = "";

    private Boolean required = false;

    private Class type = Void.class;

    @Nonnull
    public String getName()
    {
        return name;
    }

    @Nonnull
    public ParameterModel setName(final @Nonnull String name)
    {
        this.name = name;

        return this;
    }

    @Nonnull
    public String getShortDescription()
    {
        return shortDescription;
    }

    @Nonnull
    public ParameterModel setShortDescription(final @Nonnull String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }

    @Nonnull
    public Boolean getRequired()
    {
        return required;
    }

    @Nonnull
    public ParameterModel setRequired(final @Nonnull Boolean required)
    {
        this.required = required;

        return this;
    }

    @Nonnull
    public Class getType()
    {
        return type;
    }

    @Nonnull
    public ParameterModel setType(final @Nonnull Class type)
    {
        this.type = type;

        return this;
    }
}
