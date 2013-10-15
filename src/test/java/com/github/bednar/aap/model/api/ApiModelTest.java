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

        Assert.assertEquals(PubApi.class, model.getType());
    }

    @Test
    public void pathValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals("pub", model.getPath());
    }

    @Test
    public void consumesValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(1, model.getConsumes().length);
        Assert.assertEquals("application/json", model.getConsumes()[0]);
    }

    @Test
    public void producesValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(1, model.getProduces().length);
        Assert.assertEquals("application/json", model.getProduces()[0]);
    }

    @Test
    public void shortDescriptionValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals("Public Pub", model.getShortDescription());
    }

    @Test
    public void descriptionValue()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals("Long description of Public Pub!", model.getDescription());
    }
}
