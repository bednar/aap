package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jakub Bednář (06/10/2013 10:35 AM)
 */
public final class ApiModel
{
    private Integer position = 0;

    private Class type = Void.class;

    private String path = "";

    private String[] consumes = new String[]{};
    private String[] produces = new String[]{};

    private String shortDescription  = "";
    private String description       = "";

    private List<OperationModel> operations = Lists.newArrayList();

    @Nonnull
    public Integer getPosition()
    {
        return position;
    }

    @Nonnull
    public ApiModel setPosition(final @Nonnull Integer position)
    {
        this.position = position;

        return this;
    }

    @Nonnull
    public Class getType()
    {
        return type;
    }

    @Nonnull
    public ApiModel setType(final @Nonnull Class type)
    {
        this.type = type;

        return this;
    }

    @Nonnull
    public String getPath()
    {
        return path;
    }

    @Nonnull
    public ApiModel setPath(final @Nonnull String path)
    {
        this.path = path;

        return this;
    }

    @Nonnull
    public String[] getConsumes()
    {
        return consumes;
    }

    public ApiModel setConsumes(final @Nonnull String[] consumes)
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
    public ApiModel setProduces(final @Nonnull String[] produces)
    {
        this.produces = produces;

        return this;
    }

    @Nonnull
    public String getShortDescription()
    {
        return shortDescription;
    }

    @Nonnull
    public ApiModel setShortDescription(final @Nonnull String shortDescription)
    {
        this.shortDescription = shortDescription;

        return this;
    }

    @Nonnull
    public String getDescription()
    {
        return description;
    }

    @Nonnull
    public ApiModel setDescription(final @Nonnull String description)
    {
        this.description = description;

        return this;
    }

    @Nonnull
    public List<OperationModel> getOperations()
    {
        return operations;
    }

    @Nonnull
    public ApiModel setOperations(final @Nonnull List<OperationModel> operations)
    {
        this.operations = operations;

        return this;
    }
}
