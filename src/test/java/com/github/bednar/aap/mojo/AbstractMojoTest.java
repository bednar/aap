package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * @author Jakub Bednář (24/11/2013 11:34)
 */
public class AbstractMojoTest extends AbstractMojoTestCase
{
    protected File pomFile;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        String pom =
                "<project><build><plugins><plugin>" +
                "<artifactId>aap</artifactId><configuration/>" +
                "</plugin></plugins></build></project>";

        Path pomFile = Files.createTempFile("pom", ".xml");

        Files.write(pomFile, pom.getBytes());

        this.pomFile = pomFile.toFile();
    }

    @Nullable
    protected String defaultValue(@Nonnull final String parameterKey, @Nonnull final MojoExecution mojo)
    {
        return mojo.getMojoDescriptor().getParameterMap().get(parameterKey).getDefaultValue();
    }
}
