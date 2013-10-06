package com.github.bednar.aap.model.entity;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jakub Bednář (06/10/2013 2:21 PM)
 */
public final class EntityModel
{
    public String shortDescription = "";

    public List<PropertyModel> properties = Lists.newArrayList();
}
