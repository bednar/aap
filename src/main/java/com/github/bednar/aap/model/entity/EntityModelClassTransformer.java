package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
* @author Jakub Bednář (06/10/2013 2:56 PM)
*/
public class EntityModelClassTransformer implements Function<Class, EntityModel>
{
    @Nonnull
    @Override
    public EntityModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Class klass)
    {
        String shorDescription          = processShortDescription(klass);
        String description              = processDescription(klass);
        List<PropertyModel> properties  = processProperties(klass);

        EntityModel model = new EntityModel();
        model
                .setType(new TypeModel(klass))
                .setShortDescription(shorDescription)
                .setDescription(description)
                .setProperties(properties);

        return model;
    }

    @Nonnull
    private String processShortDescription(final @Nonnull Class<?> klass)
    {
        ApiModel model = klass.getAnnotation(ApiModel.class);

        return model != null ? model.value() : "";
    }

    @Nonnull
    private String processDescription(final @Nonnull Class<?> klass)
    {
        ApiModel model = klass.getAnnotation(ApiModel.class);

        return model != null ? model.description() : "";
    }

    @Nonnull
    private List<PropertyModel> processProperties(final @Nonnull Class klass)
    {
        List<Field> declaredFields = Lists.newArrayList(klass.getDeclaredFields());

        return FluentIterable
                .from(declaredFields).filter(
                        new Predicate<Field>()
                        {
                            @Override
                            public boolean apply(@Nullable final Field field)
                            {
                                return field != null && field.getAnnotation(ApiModelProperty.class) != null;
                            }
                        })
                .transform(new PropertyModelClassTransformer())
                .toSortedList(
                        propertyComparator());
    }

    static Comparator<PropertyModel> propertyComparator()
    {
        return new Comparator<PropertyModel>()
        {
            @Override
            public int compare(final PropertyModel model1, final PropertyModel model2)
            {
                return ComparisonChain.start()
                        .compare(model1.getPosition(), model2.getPosition())
                        .result();
            }
        };
    }
}
