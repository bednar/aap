package com.github.bednar.aap.model.entity;

import java.io.File;

import org.junit.Before;

/**
 * @author Jakub Bednář (06/10/2013 2:27 PM)
 */
public class EntityModelSourceTest extends AbstractEntityModelTest
{
    @Before
    public void before()
    {
        super.before();

        model = new EntityModelSourceTransformer()
                .apply(new File("./src/test/java/com/github/bednar/aap/example/Meal.java"));
    }
}
