package com.github.bednar.aap;

import com.github.bednar.aap.example.PubApi;
import com.github.bednar.aap.model.api.ParameterModel;
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
        ParameterModel model = builder.getApiModel(PubApi.class).operations.get(0).parameters.get(0);

        Assert.assertEquals("name", model.name);
        Assert.assertEquals("Name of Meal", model.shortDescription);

        Assert.assertTrue(model.required);

        Assert.assertEquals(String.class, model.type);
    }

    @Test
    public void updateMealMeal()
    {
        ParameterModel model = builder.getApiModel(PubApi.class).operations.get(1).parameters.get(0);

        Assert.assertEquals("meal", model.name);
        Assert.assertEquals("Meal for update", model.shortDescription);

        Assert.assertTrue(model.required);

        Assert.assertEquals(PubApi.Meal.class, model.type);
    }
}
