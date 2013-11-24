package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.Meal;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (16/11/2013 15:02)
 */
public abstract class AbstractEntityModelTest extends AbstractApiTest
{
    protected EntityModel model;

    @Test
    public void modelNotNull()
    {
        Assert.assertNotNull(model);
    }

    @Test
    public void typeValue()
    {
        Assert.assertEquals(Meal.class.getCanonicalName(), model.getType().getCanonicalName());
        Assert.assertEquals(Meal.class.getSimpleName(), model.getType().getSimpleName());
    }

    @Test
    public void shortDescriptionValue()
    {
        Assert.assertEquals("Tasty Meal", model.getShortDescription());
    }

    @Test
    public void propertiesSize()
    {
        Assert.assertEquals(3, model.getProperties().size());
    }
}
