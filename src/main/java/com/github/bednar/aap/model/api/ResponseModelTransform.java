package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.wordnik.swagger.annotations.ApiResponse;

/**
 * @author Jakub Bednář (28/12/2013 16:03)
 */
public final class ResponseModelTransform implements Function<ApiResponse, ResponseModel>
{
    @Nullable
    @Override
    public ResponseModel apply(@Nonnull @SuppressWarnings("NullableProblems") final ApiResponse input)
    {
        return new ResponseModel()
                .setShortDescription(input.message())
                .setStatusCode(input.code());
    }
}
