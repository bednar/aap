package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import java.lang.reflect.Field;

import com.google.common.base.Function;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author Jakub Bednář (06/10/2013 6:45 PM)
 */
public class PropertyModelTransformer implements Function<Field, PropertyModel>
{
    @Nonnull
    @Override
    public PropertyModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Field field)
    {
        PropertyModel model = new PropertyModel();

        Integer position    = processPosition(field);
        String name         = processName(field);
        Class type          = processType(field);

        Boolean required  = processRequired(field);
        Integer maxLength = processMaxLength(field);
        Integer precision = processPrecision(field);
        Integer scale     = processScale(field);

        model
                .setPosition(position)
                .setName(name)
                .setType(type)
                .setRequired(required)
                .setMaxLength(maxLength)
                .setPrecision(precision)
                .setScale(scale);

        return model;
    }

    private int processPosition(final @Nonnull Field field)
    {
        ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);

        return property != null ? property.position() : 0;
    }

    @Nonnull
    private String processName(final @Nonnull Field field)
    {
        ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);

        return property != null ? property.value() : "";
    }

    @Nonnull
    private Class processType(final @Nonnull Field field)
    {
        return field.getType();
    }

    @Nonnull
    private Boolean processRequired(final @Nonnull Field field)
    {
        Column column = field.getAnnotation(Column.class);

        return column != null && !column.nullable();
    }

    private int processMaxLength(final Field field)
    {
        Column column = field.getAnnotation(Column.class);

        return column != null ? column.length() : 0;
    }

    private int processPrecision(final Field field)
    {
        Column column = field.getAnnotation(Column.class);

        return column != null ? column.precision() : 0;
    }

    private int processScale(final Field field)
    {
        Column column = field.getAnnotation(Column.class);

        return column != null ? column.scale() : 0;
    }
}
