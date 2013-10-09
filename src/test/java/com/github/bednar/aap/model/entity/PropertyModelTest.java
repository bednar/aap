package com.github.bednar.aap.model.entity;

import java.math.BigDecimal;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (06/10/2013 6:37 PM)
 */
public class PropertyModelTest extends AbstractApiTest
{
    @Test
    public void nameValue()
    {
        PropertyModel model = builder.getEntityModel(PubApi.Meal.class).getProperties().get(1);

        Assert.assertEquals(2, model.position);

        Assert.assertEquals("Name", model.name);
        Assert.assertEquals(String.class, model.type);

        Assert.assertTrue(model.required);
        Assert.assertEquals(100, model.maxLength);

        Assert.assertEquals(0, model.precision);
        Assert.assertEquals(0, model.scale);
    }

    @Test
    public void priceValue()
    {
        PropertyModel model = builder.getEntityModel(PubApi.Meal.class).getProperties().get(0);

        Assert.assertEquals(1, model.position);

        Assert.assertEquals("Price", model.name);
        Assert.assertEquals(BigDecimal.class, model.type);

        Assert.assertFalse(model.required);
        Assert.assertEquals(255, model.maxLength);

        Assert.assertEquals(10, model.precision);
        Assert.assertEquals(2, model.scale);
    }
}
