package com.github.bednar.aap.mojo;

import java.nio.file.Files;

import com.google.common.collect.Lists;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecution;
import org.junit.Assert;

/**
 * @author Jakub Bednář (23/11/2013 13:29)
 */
public class ApiaryMojoTest extends AbstractMojoTest
{
    public void testNotNullMojo() throws Exception
    {
        Mojo apiary = lookupMojo("apiary", pomFile);

        Assert.assertNotNull(apiary);
    }

    public void testDefaultAapiaryOutput() throws Exception
    {
        MojoExecution apiary = newMojoExecution("apiary");

        Assert.assertEquals("${project.build.directory}/generated/apiary", defaultValue("apiaryOutput", apiary));
    }

    public void testDefaultAppName() throws Exception
    {
        MojoExecution apiary = newMojoExecution("apiary");

        Assert.assertEquals("Demo Application", defaultValue("appName", apiary));
    }

    public void testDefaultAppDescription() throws Exception
    {
        MojoExecution apiary = newMojoExecution("apiary");

        Assert.assertEquals("${project.basedir}/description.md", defaultValue("appDescription", apiary));
    }

    public void testDefaultApiBaseURL() throws Exception
    {
        MojoExecution apiary = newMojoExecution("apiary");

        Assert.assertEquals("http://demoaap.apiary.io", defaultValue("apiBaseURL", apiary));
    }

    public void testDefaultBlueprintName() throws Exception
    {
        MojoExecution apiary = newMojoExecution("apiary");

        Assert.assertEquals("Apiary.md", defaultValue("blueprintName", apiary));
    }

    public void testExecuteMojo() throws Exception
    {
        Mojo apiary = lookupMojo("apiary", pomFile);

        setVariableValueToObject(apiary, "classpathElements", Lists.<String>newArrayList());
        setVariableValueToObject(apiary, "apiaryOutput", Files.createTempDirectory("apiary-mojo").toFile());
        setVariableValueToObject(apiary, "appName", "Testing Mojo");
        setVariableValueToObject(apiary, "apiBaseURL", "https://www.example.com");
        setVariableValueToObject(apiary, "blueprintName", "mojo-blueprint.md");

        apiary.execute();
    }
}
