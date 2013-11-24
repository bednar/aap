package com.github.bednar.aap.model.entity;

import java.io.File;

import org.junit.Before;

/**
 * @author Jakub Bednář (16/11/2013 16:42)
 */
public class PropertyModelSourceTest extends AbstractPropertyModelTest
{
    @Before
    public void before()
    {
        super.before();

        EntityModel entityModel = new EntityModelSourceTransformer()
                .apply(new File("./src/test/java/com/github/bednar/aap/example/Meal.java"));

        //noinspection ConstantConditions
        nameModel       = entityModel.getProperties().get(1);
        priceModel      = entityModel.getProperties().get(0);
        computedModel   = entityModel.getProperties().get(2);
    }
}
