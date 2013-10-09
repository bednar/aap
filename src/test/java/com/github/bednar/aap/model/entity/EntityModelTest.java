package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (06/10/2013 2:27 PM)
 */
public class EntityModelTest extends AbstractApiTest
{
    @Test
    public void modelNotNull()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertNotNull(model);
    }

    @Test
    public void typeValue()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertEquals(PubApi.Meal.class, model.getType());
    }

    @Test
    public void shortDescriptionValue()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertEquals("Meal for order in pub.", model.getShortDescription());
    }

    @Test
    public void propertiesSize()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertEquals(2, model.getProperties().size());
    }
}
