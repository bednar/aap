package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.github.bednar.aap.model.ModelBuilder;
import com.github.bednar.aap.model.entity.EntityModel;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

/**
 * @author Jakub Bednář (08/11/2013 09:18)
 */
public abstract class AbstractProcessor
{
    protected void createDirectory(@Nonnull final File outputDirectory)
    {
        if (!outputDirectory.exists())
        {
            try
            {
                Files.createDirectories(Paths.get(outputDirectory.getAbsolutePath()));
            }
            catch (IOException e)
            {
                throw new AbstractProcessorException(e);
            }
        }
    }

    @Nonnull
    protected List<EntityModel> entityModels(@Nonnull final List<Class> entityClasses)
    {
        return FluentIterable.from(entityClasses).transform(
                new Function<Class, EntityModel>()
                {
                    @Nullable
                    @Override
                    public EntityModel apply(@Nonnull @SuppressWarnings("NullableProblems") final Class klass)
                    {
                        Preconditions.checkNotNull(klass);

                        return ModelBuilder.getInstance().getEntityModel(klass);
                    }
                }).toList();
    }

    private static class AbstractProcessorException extends RuntimeException
    {
        private AbstractProcessorException(@Nonnull final Throwable cause)
        {
            super(cause);
        }
    }
}
