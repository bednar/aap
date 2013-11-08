package com.github.bednar.aap.mojo;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.github.bednar.aap.processor.Apiary;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.reflections.Reflections;

/**
 * Generate Apiary Blueprint.
 *
 * @author Jakub Bednář (15/10/2013 4:24 PM)
 */
@Mojo(name = "apiary")
public class ApiaryMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> sourceCompiledPaths;

    /**
     * Documentation output
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/apiary")
    private File apiaryOutput;

    /**
     * Name of Application
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "Demo Application")
    private String appName;

    /**
     * File with Description (support <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>) of Application
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "${project.basedir}/description.md")
    private File appDescription;

    /**
     * API Base URL
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "http://demoaap.apiary.io")
    private String apiBaseURL;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info(String.format("[apiary-cfg][appName:%s][apiBaseURL:%s][appDescription:%s]",
                appName, apiBaseURL, appDescription));

        Reflections reflections = getReflections(sourceCompiledPaths);

        Collection<Class<?>> apis       = reflections.getTypesAnnotatedWith(Api.class);
        Collection<Class<?>> entities   = reflections.getTypesAnnotatedWith(ApiModel.class);

        Apiary
                .create(apiaryOutput)
                .addApis(apis)
                .addEntities(entities)
                .generate(appName, apiBaseURL, appDescription);
    }
}
