package com.github.bednar.aap.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Generate Data Transfer Object (DTO) from {@link com.wordnik.swagger.annotations.ApiModel}.
 *
 * @author Jakub Bednář (06/11/2013 17:12)
 */
@Mojo(name = "dto")
public class DTOMojo extends AbstractMojo
{
    /**
     * DTO classes output directory.
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/dto")
    private File dtoOutput;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
    }
}
