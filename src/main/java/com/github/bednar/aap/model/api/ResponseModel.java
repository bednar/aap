package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;

/**
 * @author Jakub Bednář (28/12/2013 15:54)
 */
public final class ResponseModel
{
    private Integer statusCode = 0;

    private String shortDescription = "";

    @Nonnull
    public Integer getStatusCode()
    {
        return statusCode;
    }

    @Nonnull
    public ResponseModel setStatusCode(@Nonnull final Integer statusCode)
    {
        this.statusCode = statusCode;

        return this;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    @Nonnull
    public ResponseModel setShortDescription(@Nonnull final String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }
}
