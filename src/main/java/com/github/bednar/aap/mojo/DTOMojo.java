package com.github.bednar.aap.mojo;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.github.bednar.aap.processor.DTO;
import com.wordnik.swagger.annotations.ApiModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.reflections.Reflections;

/**
 * Generate Data Transfer Object (DTO) from {@link com.wordnik.swagger.annotations.ApiModel}.
 *
 * @author Jakub Bednář (06/11/2013 17:12)
 */
@Mojo(name = "dto")
public class DTOMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> sourceCompiledPaths;

    @Parameter(defaultValue = "${project.testClasspathElements}", required = true, readonly = true)
    private List<String> testSourceCompliledPaths;

    /**
     * DTO classes output directory.
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/dto")
    private File dtoOutput;

    /**
     * If {@link Boolean#TRUE} than use Test-Classpath for search {@link com.wordnik.swagger.annotations.ApiModel}.
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "false")
    private Boolean addTestClasspath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        Reflections reflections;

        if (addTestClasspath)
        {
            reflections = getReflections(sourceCompiledPaths, testSourceCompliledPaths);
        }
        else
        {
            reflections = getReflections(sourceCompiledPaths);
        }

        Collection<Class<?>> entities = reflections.getTypesAnnotatedWith(ApiModel.class);

        DTO
                .create(dtoOutput)
                .addEntities(entities)
                .generate();
    }
}
