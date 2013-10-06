package com.github.bednar.aap.model.entity;

/**
 * @author Jakub Bednář (06/10/2013 2:22 PM)
 */
public final class PropertyModel
{
    public int position = 0;

    public String name  = "";
    public Class type   = Void.class;

    public Boolean required = false;

    /**
     * String
     */
    public int maxLength = 0;

    /**
     * Number
     */
    public int precision = 0;
    public int scale = 0;
}
