package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.example.Meal;
import org.junit.Before;

/**
 * @author Jakub Bednář (06/10/2013 2:27 PM)
 */
public class EntityModelClassTest extends AbstractEntityModelTest
{
    @Before
    public void before()
    {
        super.before();

        model = builder.getEntityModelClass(Meal.class);
    }
}
