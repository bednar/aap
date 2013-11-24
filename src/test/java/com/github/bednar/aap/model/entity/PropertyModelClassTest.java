package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.example.Meal;
import org.junit.Before;

/**
 * @author Jakub Bednář (06/10/2013 6:37 PM)
 */
public class PropertyModelClassTest extends AbstractPropertyModelTest
{
    @Before
    public void before()
    {
        super.before();

        nameModel       = builder.getEntityModelClass(Meal.class).getProperties().get(1);
        priceModel      = builder.getEntityModelClass(Meal.class).getProperties().get(0);
        computedModel   = builder.getEntityModelClass(Meal.class).getProperties().get(2);
    }
}
