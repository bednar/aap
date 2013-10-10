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

        Assert.assertEquals(2, model.getPosition().intValue());

        Assert.assertEquals("Name", model.getShortDescription());
        Assert.assertEquals(String.class, model.getType());

        Assert.assertTrue(model.getRequired());
        Assert.assertEquals(100, model.getMaxLength().intValue());

        Assert.assertEquals(0, model.getPrecision().intValue());
        Assert.assertEquals(0, model.getScale().intValue());
    }

    @Test
    public void priceValue()
    {
        PropertyModel model = builder.getEntityModel(PubApi.Meal.class).getProperties().get(0);

        Assert.assertEquals(1, model.getPosition().intValue());

        Assert.assertEquals("Price", model.getShortDescription());
        Assert.assertEquals(BigDecimal.class, model.getType());

        Assert.assertFalse(model.getRequired());
        Assert.assertEquals(255, model.getMaxLength().intValue());

        Assert.assertEquals(10, model.getPrecision().intValue());
        Assert.assertEquals(2, model.getScale().intValue());
    }
}
