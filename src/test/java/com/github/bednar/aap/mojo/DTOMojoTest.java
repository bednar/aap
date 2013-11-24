package com.github.bednar.aap.mojo;

import java.nio.file.Files;

import com.google.common.collect.Lists;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecution;
import org.junit.Assert;

/**
 * @author Jakub Bednář (24/11/2013 11:34)
 */
public class DTOMojoTest extends AbstractMojoTest
{
    public void testMojoNotNull() throws Exception
    {
        Mojo mojo = lookupMojo("dto", pomFile);

        Assert.assertNotNull(mojo);
    }

    public void testDefaultSourcePaths()
    {
        MojoExecution dto = newMojoExecution("dto");

        Assert.assertEquals("${project.compileSourceRoots}", defaultValue("sourcePaths", dto));
    }

    public void testTestSourcePath()
    {
        MojoExecution dto = newMojoExecution("dto");

        Assert.assertEquals("${project.build.testSourceDirectory}", defaultValue("testSourcePath", dto));
    }

    public void testDtoOutput()
    {
        MojoExecution dto = newMojoExecution("dto");

        Assert.assertEquals("${project.build.directory}/generated/dto", defaultValue("dtoOutput", dto));
    }

    public void testAddTestSources()
    {
        MojoExecution dto = newMojoExecution("dto");

        Assert.assertEquals("false", defaultValue("addTestSources", dto));
    }

    public void testExecuteMojo() throws Exception
    {
        Mojo dto = lookupMojo("dto", pomFile);

        setVariableValueToObject(dto, "sourcePaths", Lists.<String>newArrayList());
        setVariableValueToObject(dto, "dtoOutput", Files.createTempDirectory("dto-mojo").toFile());
        setVariableValueToObject(dto, "addTestSources", false);

        dto.execute();
    }
}
