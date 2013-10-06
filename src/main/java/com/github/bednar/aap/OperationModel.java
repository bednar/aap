package com.github.bednar.aap;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Jakub Bednář (06/10/2013 11:10 AM)
 */
public class OperationModel
{
    public int position = 0;

    public String path = "";
    public String httpMethod = "";

    public String shortDescription = "";
    public String authorizations = "";

    public Class responseEntity = Void.class;
    public Class responseWrapper = Void.class;
    public Map<Integer, String> responses = Maps.newHashMap();

    public List<Object> parameters = Lists.newArrayList();
}
