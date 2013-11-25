package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.bednar.aap.model.entity.EntityModel;
import com.github.bednar.aap.model.entity.EntityModelSourceTransformer;
import com.github.bednar.aap.processor.DTO;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

/**
 * Generate Data Transfer Object (DTO) from {@link com.wordnik.swagger.annotations.ApiModel}.
 *
 * @author Jakub Bednář (06/11/2013 17:12)
 */
@Mojo(name = "dto")
public class DTOMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.compileSourceRoots}", required = true, readonly = true)
    private List<String> sourcePaths;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", required = true, readonly = true)
    private String testSourcePath;

    /**
     * DTO classes output directory.
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/dto")
    private File dtoOutput;

    /**
     * If {@link Boolean#TRUE} than use test sources path for search {@link com.wordnik.swagger.annotations.ApiModel}.
     *
     * @since 0.1
     */
    @Parameter(defaultValue = "false")
    private Boolean addTestSources;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        List<File> sourceFiles = Lists.newArrayList();

        for (String sourcePath : sourcePaths)
        {
            sourceFiles.addAll(getFiles(sourcePath));
        }

        if (addTestSources)
        {
            sourceFiles.addAll(getFiles(testSourcePath));
        }

        List<EntityModel> entityModels = FluentIterable
                .from(sourceFiles)
                .transform(new Function<File, EntityModel>()
                {
                    @Nullable
                    @Override
                    public EntityModel apply(@Nullable final File input)
                    {
                        getLog().info("[lookup-EntityModel][" + input + "]");

                        return new EntityModelSourceTransformer().apply(input);
                    }
                })
                .filter(new Predicate<EntityModel>()
                {
                    @Override
                    public boolean apply(@Nullable final EntityModel input)
                    {
                        return input != null;
                    }
                })
                .toList();

        DTO
                .create(dtoOutput, getLog())
                .addEntityModels(entityModels)
                .generate();
    }

    @Nonnull
    private List<File> getFiles(@Nonnull final String sourcePath)
    {
        try
        {
            File sourceDirectory = new File(sourcePath);

            if (sourceDirectory.exists())
            {
                //noinspection unchecked
                return FileUtils.getFiles(sourceDirectory, "**/*.java", "");
            }
            else
            {
                return Lists.newArrayList();
            }

        }
        catch (IOException e)
        {
            throw new DTOMojoException(e);
        }
    }

    private class DTOMojoException extends RuntimeException
    {
        private DTOMojoException(final Throwable cause)
        {
            super(cause);
        }
    }
}
