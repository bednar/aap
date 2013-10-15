package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @author Jakub Bednář (06/10/2013 2:45 PM)
 */
public class ParameterModelTransform implements Function<ParameterModelTransform.MethodParameter, ParameterModel>
{
    @Nullable
    @Override
    public ParameterModel apply(final @Nonnull @SuppressWarnings("NullableProblems") MethodParameter methodParameter)
    {
        String name              = processName(methodParameter);
        String shortDescription  = processShortDescription(methodParameter);

        Boolean required = processRequired(methodParameter);

        Class type = processType(methodParameter);

        ParameterModel model = new ParameterModel();

        model
                .setName(name)
                .setShortDescription(shortDescription)
                .setRequired(required)
                .setType(type);

        return model;
    }

    @Nonnull
    private String processName(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null ? methodParameter.param.name() : "";
    }

    @Nonnull
    private String processShortDescription(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null ? methodParameter.param.value() : "";
    }

    @Nonnull
    private Boolean processRequired(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null && methodParameter.param.required();
    }

    @Nonnull
    private Class processType(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.type != null ? methodParameter.type : Void.class;
    }

    public static class MethodParameter
    {
        public Class type;
        public ApiParam param;
    }
}
