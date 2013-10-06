package com.github.bednar.aap.transform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.bednar.aap.model.api.ParameterModel;
import com.google.common.base.Function;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @author Jakub Bednář (06/10/2013 2:45 PM)
 */
public class ApiParamTransform implements Function<ApiParamTransform.MethodParameter, ParameterModel>
{
    @Nullable
    @Override
    public ParameterModel apply(final @Nonnull @SuppressWarnings("NullableProblems") MethodParameter methodParameter)
    {
        ParameterModel model = new ParameterModel();

        model.name              = processName(methodParameter);
        model.shortDescription  = processShortDescription(methodParameter);

        model.required = processRequired(methodParameter);

        model.type = processType(methodParameter);

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
