package com.github.bednar.aap.model.api;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (06/10/2013 10:37 AM)
 */
public class ApiModelTest extends AbstractApiTest
{
    @Test
    public void builderNotNull()
    {
        Assert.assertNotNull(builder);
    }

    @Test
    public void apiModelNotNull()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertNotNull(model);
    }

    @Test
    public void typeValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(PubApi.class, model.type);
    }

    @Test
    public void pathValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals("pub", model.path);
    }

    @Test
    public void consumesValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(1, model.consumes.length);
        Assert.assertEquals("application/json", model.consumes[0]);
    }

    @Test
    public void producesValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(1, model.produces.length);
        Assert.assertEquals("application/json", model.produces[0]);
    }

    @Test
    public void shortDescriptionValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals("Public Pub", model.shortDescription);
    }
}
