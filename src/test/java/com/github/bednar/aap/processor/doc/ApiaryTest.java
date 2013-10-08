package com.github.bednar.aap.processor.doc;

import java.io.File;
import java.util.Arrays;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.PubApi;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jakub Bednář (07/10/2013 7:51 PM)
 */
public class ApiaryTest extends AbstractApiTest
{
    private static final Logger LOG = LoggerFactory.getLogger(ApiaryTest.class);

    private File outputDirectory;
    private Apiary apiary;

    @Before
    public void before()
    {
        outputDirectory = Files.createTempDir();

        apiary = Apiary.create(outputDirectory);
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

    @Test
    public void createdFilesCount()
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(PubApi.Meal.class)
                .generate();

        LOG.info("[file-list][{}]", Arrays.toString(outputDirectory.list()));

        //doc for PubApi + doc for Meal + join doc
        Assert.assertEquals(3, outputDirectory.list().length);
    }
}
