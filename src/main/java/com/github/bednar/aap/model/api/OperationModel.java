package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Jakub Bednář (06/10/2013 11:10 AM)
 */
public final class OperationModel
{
    private Integer position = 0;

    private String path = "";
    private String httpMethod = "";

    private String shortDescription = "";
    private String authorizations = "";

    private Class responseEntity = Void.class;
    private Class responseWrapper = Void.class;
    private Map<String, String> responses = Maps.newHashMap();

    private List<ParameterModel> parameters = Lists.newArrayList();

    @Nonnull
    public Integer getPosition()
    {
        return position;
    }

    @Nonnull
    public OperationModel setPosition(final @Nonnull Integer position)
    {
        this.position = position;

        return this;
    }

    @Nonnull
    public String getPath()
    {
        return path;
    }

    @Nonnull
    public OperationModel setPath(final @Nonnull String path)
    {
        this.path = path;

        return this;
    }

    @Nonnull
    public String getHttpMethod()
    {
        return httpMethod;
    }

    @Nonnull
    public OperationModel setHttpMethod(final @Nonnull String httpMethod)
    {
        this.httpMethod = httpMethod;

        return this;
    }

    @Nonnull
    public String getShortDescription()
    {
        return shortDescription;
    }

    @Nonnull
    public OperationModel setShortDescription(final @Nonnull String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }

    @Nonnull
    public String getAuthorizations()
    {
        return authorizations;
    }

    @Nonnull
    public OperationModel setAuthorizations(final @Nonnull String authorizations)
    {
        this.authorizations = authorizations;

        return this;
    }

    @Nonnull
    public Class getResponseEntity()
    {
        return responseEntity;
    }

    @Nonnull
    public OperationModel setResponseEntity(final @Nonnull Class responseEntity)
    {
        this.responseEntity = responseEntity;

        return this;
    }

    @Nonnull
    public Class getResponseWrapper()
    {
        return responseWrapper;
    }

    @Nonnull
    public OperationModel setResponseWrapper(final @Nonnull Class responseWrapper)
    {
        this.responseWrapper = responseWrapper;

        return this;
    }

    @Nonnull
    public Map<String, String> getResponses()
    {
        return responses;
    }

    @Nonnull
    public OperationModel setResponses(final @Nonnull Map<String, String> responses)
    {
        this.responses = responses;

        return this;
    }

    @Nonnull
    public List<ParameterModel> getParameters()
    {
        return parameters;
    }

    @Nonnull
    public OperationModel setParameters(final @Nonnull List<ParameterModel> parameters)
    {
        this.parameters = parameters;

        return this;
    }
}
