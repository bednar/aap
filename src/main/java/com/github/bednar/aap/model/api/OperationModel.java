package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jakub Bednář (06/10/2013 11:10 AM)
 */
public final class OperationModel
{
    private Integer position = 0;

    private String path         = "";
    private String httpMethod   = "";

    private String shortDescription = "";
    private String authorizations   = "";

    private String[] consumes = new String[]{};
    private String[] produces = new String[]{};

    private Class responseEntity            = Void.class;
    private Class responseWrapper           = Void.class;
    private List<ResponseModel> responses   = Lists.newArrayList();

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
    public String[] getConsumes()
    {
        return consumes;
    }

    @Nonnull
    public OperationModel setConsumes(final @Nonnull String[] consumes)
    {
        this.consumes = consumes;

        return this;
    }

    @Nonnull
    public String[] getProduces()
    {
        return produces;
    }

    @Nonnull
    public OperationModel setProduces(final @Nonnull String[] produces)
    {
        this.produces = produces;

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
    public List<ResponseModel> getResponses()
    {
        return responses;
    }

    @Nonnull
    public OperationModel setResponses(final @Nonnull List<ResponseModel> responses)
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
