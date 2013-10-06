package com.github.bednar.aap.entity;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import com.github.bednar.aap.model.entity.EntityModel;
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
    public void shortDescriptionValue()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertEquals("Meal for order in pub.", model.shortDescription);
    }

    @Test
    public void propertiesSize()
    {
        EntityModel model = builder.getEntityModel(PubApi.Meal.class);

        Assert.assertEquals(2, model.properties.size());
    }
}
