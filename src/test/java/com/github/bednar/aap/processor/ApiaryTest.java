package com.github.bednar.aap.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.github.bednar.aap.AbstractApiTest;
import com.github.bednar.aap.example.Meal;
import com.github.bednar.aap.example.PubApi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

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
        outputDirectory = com.google.common.io.Files.createTempDir();

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
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);
    }

    @Test
    public void createdFilesCount()
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);

        LOG.info("[file-list][{}]", Arrays.toString(outputDirectory.list()));

        //doc for PubApi + doc for Meal + join doc
        Assert.assertEquals(3, outputDirectory.list().length);
    }

    @Test
    public void createdFilesName()
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);

        Assert.assertTrue(Files.exists(Paths.get(outputDirectory.getAbsolutePath(), "PubApi.md")));
        Assert.assertTrue(Files.exists(Paths.get(outputDirectory.getAbsolutePath(), "Meal.md")));
        Assert.assertTrue(Files.exists(Paths.get(outputDirectory.getAbsolutePath(), "Apiary.md")));
    }

    @Test
    public void mealDocValue() throws IOException
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);

        List<String> lines = Files.readAllLines(Paths.get(outputDirectory.getAbsolutePath(), "Meal.md"), UTF_8);

        Assert.assertFalse(lines.isEmpty());
        Assert.assertEquals("--", lines.get(0));
        Assert.assertEquals("Resource: Tasty Meal", lines.get(1));
        Assert.assertEquals("Delicious czech meal", lines.get(2));
        Assert.assertEquals("### Properties", lines.get(3));
        Assert.assertEquals("- `price[bigdecimal precision:10, scale:2]` - _Price_", lines.get(4));
        Assert.assertEquals("- `name[string length:100, required]` - _Name_", lines.get(5));
        Assert.assertEquals("- `computedField[string transient]` - _Computed field_", lines.get(6));
        Assert.assertEquals("--", lines.get(7));
    }

    @Test
    public void pubApiValue() throws IOException

    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);

        List<String> lines = Files.readAllLines(Paths.get(outputDirectory.getAbsolutePath(), "PubApi.md"), UTF_8);

        Assert.assertFalse(lines.isEmpty());
        Assert.assertEquals("--", lines.get(0));
        Assert.assertEquals("Public Pub", lines.get(1));
        Assert.assertEquals("Long description of Public Pub!", lines.get(2));
        Assert.assertEquals("--", lines.get(3));
    }

    @Test
    public void apiaryValue() throws IOException
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null);

        List<String> lines = Files.readAllLines(Paths.get(outputDirectory.getAbsolutePath(), "Apiary.md"), UTF_8);

        Assert.assertFalse(lines.isEmpty());
    }

    @Test
    public void customBlueprintNAme()
    {
        apiary
                .addApis(PubApi.class)
                .addEntities(Meal.class)
                .generate("Demo Application", "http://www.example.com", null, "apiary.apib");

        File blueprint = new File(outputDirectory.getAbsolutePath() + File.separator + "apiary.apib");

        Assert.assertTrue(blueprint.exists());
        Assert.assertTrue(blueprint.canRead());
    }
}
