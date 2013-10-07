package com.github.bednar.aap.processor.doc;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jakub Bednář (07/10/2013 7:51 PM)
 */
public class ApiaryTest extends AbstractApiTest
{
    private Apiary apiary;

    @Before
    public void before()
    {
        apiary = Apiary.create();
    }

    @Test
    public void apiaryNotNull()
    {
        Assert.assertNotNull(apiary);
    }

    @Test
    public void canGenerate()
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(PubApi.Meal.class)
                .generate();
    }
}
