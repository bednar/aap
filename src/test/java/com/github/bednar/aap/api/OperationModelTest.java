package com.github.bednar.aap.api;

import java.util.List;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import com.github.bednar.aap.model.api.ApiModel;
import com.github.bednar.aap.model.api.OperationModel;
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

        Assert.assertEquals(3, model.operations.size());
    }

    @Test
    public void mealByNameValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).operations.get(0);

        Assert.assertEquals(1, model.position);

        Assert.assertEquals("GET", model.httpMethod);
        Assert.assertEquals("pub/mealByName", model.path);

        Assert.assertEquals("Find meal by name.", model.shortDescription);
        Assert.assertEquals("", model.authorizations);

        Assert.assertEquals(PubApi.Meal.class, model.responseEntity);
        Assert.assertEquals(Void.class, model.responseWrapper);

        Assert.assertEquals(1, model.responses.size());
        Assert.assertEquals("Cannot find meal by name.", model.responses.get(400));

        Assert.assertEquals(1, model.parameters.size());
    }

    @Test
    public void allMealsValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).operations.get(2);

        Assert.assertEquals(3, model.position);

        Assert.assertEquals("GET", model.httpMethod);
        Assert.assertEquals("pub/allMeals", model.path);

        Assert.assertEquals("Find all meals.", model.shortDescription);
        Assert.assertEquals("", model.authorizations);

        Assert.assertEquals(PubApi.Meal.class, model.responseEntity);
        Assert.assertEquals(List.class, model.responseWrapper);

        Assert.assertEquals(0, model.responses.size());

        Assert.assertEquals(0, model.parameters.size());
    }

    @Test
    public void updateMealValue()
    {
        OperationModel model = builder.getApiModel(PubApi.class).operations.get(1);

        Assert.assertEquals(2, model.position);

        Assert.assertEquals("POST", model.httpMethod);
        Assert.assertEquals("pub/updateMeal", model.path);

        Assert.assertEquals("Update Meal in pub.", model.shortDescription);
        Assert.assertEquals("Owner of Pub", model.authorizations);

        Assert.assertEquals(Void.class, model.responseEntity);
        Assert.assertEquals(Void.class, model.responseWrapper);

        Assert.assertEquals(2, model.responses.size());
        Assert.assertEquals("Success update.", model.responses.get(200));
        Assert.assertEquals("Authorization violation.", model.responses.get(401));

        Assert.assertEquals(1, model.parameters.size());
    }
}
