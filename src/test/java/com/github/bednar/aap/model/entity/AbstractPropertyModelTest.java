package com.github.bednar.aap.model.entity;

import java.math.BigDecimal;

import com.github.bednar.aap.AbstractApiTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jakub Bednář (16/11/2013 15:05)
 */
public abstract class AbstractPropertyModelTest extends AbstractApiTest
{
    protected PropertyModel nameModel;
    protected PropertyModel priceModel;
    protected PropertyModel computedModel;

    @Test
    public void nameValue()
    {
        Assert.assertEquals(2, nameModel.getPosition().intValue());

        Assert.assertEquals("Name", nameModel.getShortDescription());
        Assert.assertEquals("name", nameModel.getName());
        Assert.assertEquals(String.class.getSimpleName(), nameModel.getType().getSimpleName());
        Assert.assertEquals(String.class.getCanonicalName(), nameModel.getType().getCanonicalName());

        Assert.assertFalse(nameModel.getIsTransient());
        Assert.assertTrue(nameModel.getRequired());
        Assert.assertEquals(100, nameModel.getMaxLength().intValue());

        Assert.assertEquals(0, nameModel.getPrecision().intValue());
        Assert.assertEquals(0, nameModel.getScale().intValue());

        Assert.assertEquals("length:100", nameModel.getRestrictions().get(0));
        Assert.assertEquals("required", nameModel.getRestrictions().get(1));
    }

    @Test
    public void priceValue()
    {
        Assert.assertEquals(1, priceModel.getPosition().intValue());

        Assert.assertEquals("Price", priceModel.getShortDescription());
        Assert.assertEquals("price", priceModel.getName());
        Assert.assertEquals(BigDecimal.class.getSimpleName(), priceModel.getType().getSimpleName());
        Assert.assertEquals(BigDecimal.class.getCanonicalName(), priceModel.getType().getCanonicalName());

        Assert.assertFalse(priceModel.getIsTransient());
        Assert.assertFalse(priceModel.getRequired());
        Assert.assertEquals(255, priceModel.getMaxLength().intValue());

        Assert.assertEquals(10, priceModel.getPrecision().intValue());
        Assert.assertEquals(2, priceModel.getScale().intValue());

        Assert.assertEquals("precision:10", priceModel.getRestrictions().get(0));
        Assert.assertEquals("scale:2", priceModel.getRestrictions().get(1));
    }

    @Test
    public void computedValue()
    {
        Assert.assertTrue(computedModel.getIsTransient());

        Assert.assertEquals(1, computedModel.getRestrictions().size());
        Assert.assertEquals("transient", computedModel.getRestrictions().get(0));
    }
}
