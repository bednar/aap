package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.Meal;
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
        EntityModel model = builder.getEntityModel(Meal.class);

        Assert.assertNotNull(model);
    }

    @Test
    public void typeValue()
    {
        EntityModel model = builder.getEntityModel(Meal.class);

        Assert.assertEquals(Meal.class.getCanonicalName(), model.getType().getCanonicalName());
        Assert.assertEquals(Meal.class.getSimpleName(), model.getType().getSimpleName());
    }

    @Test
    public void shortDescriptionValue()
    {
        EntityModel model = builder.getEntityModel(Meal.class);

        Assert.assertEquals("Tasty Meal", model.getShortDescription());
    }

    @Test
    public void propertiesSize()
    {
        EntityModel model = builder.getEntityModel(Meal.class);

        Assert.assertEquals(2, model.getProperties().size());
    }
}
