package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Jakub Bednář (16/11/2013 12:53)
 */
public class TypeModel
{
    private final String cannonicalName;

    public TypeModel(@Nonnull final String cannonicalName)
    {
        Preconditions.checkNotNull(cannonicalName);

        this.cannonicalName = cannonicalName;
    }

    public TypeModel(@Nonnull final Class type)
    {
        this(type.getCanonicalName());
    }

    @Nonnull
    public String getCanonicalName()
    {
        return cannonicalName;
    }

    @Nonnull
    public String getSimpleName()
    {
        return StringUtils.substringAfterLast(cannonicalName, ".");
    }
}
