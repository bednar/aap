package com.github.bednar.aap.model.api;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jakub Bednář (06/10/2013 10:35 AM)
 */
public final class ApiModel
{
    public Class type = Void.class;

    public String path = "";

    public String[] consumes = new String[]{};
    public String[] produces = new String[]{};

    public String shortDescription = "";

    public List<OperationModel> operations = Lists.newArrayList();
}
