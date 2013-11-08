package com.github.bednar.aap.mojo;

import java.io.File;
import java.util.List;

import com.github.bednar.aap.processor.DTO;
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
    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> sourceCompiledPaths;

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
        DTO
                .create(dtoOutput);
    }
}
