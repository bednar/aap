package com.github.bednar.aap;

import com.github.bednar.aap.model.ModelBuilder;
import org.junit.Before;

/**
 * @author Jakub Bednář (06/10/2013 10:37 AM)
 */
public abstract class AbstractApiTest
{
    protected ModelBuilder builder;

    @Before
    public void before()
    {
        builder = ModelBuilder.getInstance();
    }
}
