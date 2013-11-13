package com.github.bednar.aap.model.api;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.Meal;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (06/10/2013 1:25 PM)
 */
public class ParameterModelTest extends AbstractApiTest
{
    @Test
    public void mealByNameNameValue()
    {
        ParameterModel model = builder.getApiModel(PubApi.class).getOperations().get(0).getParameters().get(0);

        Assert.assertEquals("name", model.getName());
        Assert.assertEquals("Name of Meal", model.getShortDescription());

        Assert.assertTrue(model.getRequired());

        Assert.assertEquals(String.class, model.getType());
    }

    @Test
    public void updateMealMeal()
    {
        ParameterModel model = builder.getApiModel(PubApi.class).getOperations().get(1).getParameters().get(0);

        Assert.assertEquals("meal", model.getName());
        Assert.assertEquals("Meal for update", model.getShortDescription());

        Assert.assertTrue(model.getRequired());

        Assert.assertEquals(Meal.class, model.getType());
    }
}
