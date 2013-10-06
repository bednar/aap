package com.github.bednar.aap.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * TODO jb rozpackagovat entity modely a api modely
 *
 * @author Jakub Bednář (06/10/2013 2:21 PM)
 */
public final class EntityModel
{
    public String shortDescription = "";

    public List<PropertyModel> properties = Lists.newArrayList();
}
