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
    protected EntityModel mealModel;
    protected EntityModel drinkModel;

    @Test
    public void modelNotNull()
    {
        Assert.assertNotNull(mealModel);
    }

    @Test
    public void typeValue()
    {
        Assert.assertEquals(Meal.class.getCanonicalName(), mealModel.getType().getCanonicalName());
        Assert.assertEquals(Meal.class.getSimpleName(), mealModel.getType().getSimpleName());
    }

    @Test
    public void shortDescriptionValue()
    {
        Assert.assertEquals("Tasty Meal", mealModel.getShortDescription());
    }

    @Test
    public void shortDescriptionValueSimpleAnnotation()
    {
        Assert.assertEquals("Beer", drinkModel.getShortDescription());
    }

    @Test
    public void descriptionValue()
    {
        Assert.assertEquals("Delicious czech meal", mealModel.getDescription());
    }

    @Test
    public void descriptionValueSimpleAnnotation()
    {
        Assert.assertEquals("", drinkModel.getDescription());
    }

    @Test
    public void propertiesSize()
    {
        Assert.assertEquals(3, mealModel.getProperties().size());
    }
}
