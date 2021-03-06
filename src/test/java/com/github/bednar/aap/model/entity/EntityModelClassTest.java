package com.github.bednar.aap.model.entity;

import com.github.bednar.aap.example.Drink;
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

        mealModel   = builder.getEntityModelClass(Meal.class);
        drinkModel  = builder.getEntityModelClass(Drink.class);
    }
}
