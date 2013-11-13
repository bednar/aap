package com.github.bednar.aap.model.api;

import java.util.List;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.Meal;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (06/10/2013 11:48 AM)
 */
public class OperationModelTest extends AbstractApiTest
{
    @Test
    public void operationsSize()
    {
        ApiModel model = builder.getApiModel(PubApi.class);

        Assert.assertEquals(3, model.getOperations().size());
    }

    @Test
    public void mealByNameValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).getOperations().get(0);

        Assert.assertEquals(1, model.getPosition().intValue());

        Assert.assertEquals("GET", model.getHttpMethod());
        Assert.assertEquals("pub/mealByName", model.getPath());

        Assert.assertEquals("Find meal by name.", model.getShortDescription());
        Assert.assertEquals("", model.getAuthorizations());

        Assert.assertEquals(Meal.class, model.getResponseEntity());
        Assert.assertEquals(Void.class, model.getResponseWrapper());

        Assert.assertEquals(1, model.getResponses().size());
        Assert.assertEquals("Cannot find meal by name.", model.getResponses().get("400"));

        Assert.assertEquals(1, model.getParameters().size());
    }

    @Test
    public void allMealsValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).getOperations().get(2);

        Assert.assertEquals(3, model.getPosition().intValue());

        Assert.assertEquals("GET", model.getHttpMethod());
        Assert.assertEquals("pub/allMeals", model.getPath());

        Assert.assertEquals("Find all meals.", model.getShortDescription());
        Assert.assertEquals("", model.getAuthorizations());

        Assert.assertEquals(Meal.class, model.getResponseEntity());
        Assert.assertEquals(List.class, model.getResponseWrapper());

        Assert.assertEquals(1, model.getResponses().size());

        Assert.assertEquals(0, model.getParameters().size());
    }

    @Test
    public void updateMealValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).getOperations().get(1);

        Assert.assertEquals(2, model.getPosition().intValue());

        Assert.assertEquals("POST", model.getHttpMethod());
        Assert.assertEquals("pub/updateMeal", model.getPath());

        Assert.assertEquals("Update Meal in pub.", model.getShortDescription());
        Assert.assertEquals("Owner of Pub", model.getAuthorizations());

        Assert.assertEquals(Void.class, model.getResponseEntity());
        Assert.assertEquals(Void.class, model.getResponseWrapper());

        Assert.assertEquals(2, model.getResponses().size());
        Assert.assertEquals("Success update.", model.getResponses().get("200"));
        Assert.assertEquals("Authorization violation.", model.getResponses().get("401"));

        Assert.assertEquals(1, model.getParameters().size());
    }
}
