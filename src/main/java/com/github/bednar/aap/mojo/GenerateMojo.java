package com.github.bednar.aap.mojo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.github.bednar.aap.processor.doc.Apiary;
import com.google.common.io.Files;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Jakub Bednář (15/10/2013 4:24 PM)
 */
@Mojo(name = "generate")
public class GenerateMojo extends AbstractMojo
{
    /**
     * Documentation output
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/apiary")
    private File apiaryOutput;

    /**
     * Name of Application
     */
    @Parameter(defaultValue = "Demo Application")
    private String appName;

    /**
     * File with Description (support <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>) of Application
     */
    @Parameter(defaultValue = "${project.basedir}/description.md")
    private File appDescription;

    /**
     * API Base URL
     */
    @Parameter(defaultValue = "http://demoaap.apiary.io")
    private String apiBaseURL;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info(String.format("[apiary-cfg][appName:%s][apiBaseURL:%s][appDescription:%s]", appName, apiBaseURL, appDescription));

        if (appDescription.exists())
        {
            try
            {
                getLog().info(Files.toString(appDescription, StandardCharsets.UTF_8));
            }
            catch (IOException e)
            {
                getLog().error(e);
            }
        }

        Apiary
                .create(apiaryOutput)
                .generate(appName, apiBaseURL, appDescription);
    }
}
