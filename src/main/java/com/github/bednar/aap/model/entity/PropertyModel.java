package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;

/**
 * @author Jakub Bednář (06/10/2013 2:22 PM)
 */
public final class PropertyModel
{
    private Integer position = 0;

    private String name             = "";
    private String shortDescription = "";
    private Class type              = Void.class;

    private Boolean required = false;

    //For String
    private Integer maxLength = 0;
    //For Number
    private Integer precision = 0;
    private Integer scale = 0;

    @Nonnull
    public Integer getPosition()
    {
        return position;
    }

    @Nonnull
    public PropertyModel setPosition(final @Nonnull Integer position)
    {
        this.position = position;

        return this;
    }

    @Nonnull
    public String getName()
    {
        return name;
    }

    @Nonnull
    public PropertyModel setName(final @Nonnull String name)
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
    public PropertyModel setShortDescription(final @Nonnull String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }

    @Nonnull
    public Class getType()
    {
        return type;
    }

    @Nonnull
    public PropertyModel setType(final @Nonnull Class type)
    {
        this.type = type;

        return this;
    }

    @Nonnull
    public Boolean getRequired()
    {
        return required;
    }

    @Nonnull
    public PropertyModel setRequired(final @Nonnull Boolean required)
    {
        this.required = required;

        return this;
    }

    @Nonnull
    public Integer getMaxLength()
    {
        return maxLength;
    }

    @Nonnull
    public PropertyModel setMaxLength(final @Nonnull Integer maxLength)
    {
        this.maxLength = maxLength;

        return this;
    }

    @Nonnull
    public Integer getPrecision()
    {
        return precision;
    }

    @Nonnull
    public PropertyModel setPrecision(final @Nonnull Integer precision)
    {
        this.precision = precision;

        return this;
    }

    @Nonnull
    public Integer getScale()
    {
        return scale;
    }

    @Nonnull
    public PropertyModel setScale(final @Nonnull Integer scale)
    {
        this.scale = scale;

        return this;
    }
}
