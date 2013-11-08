package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    private static class AbstractProcessorException extends RuntimeException
    {
        private AbstractProcessorException(@Nonnull final Throwable cause)
        {
            super(cause);
        }
    }
}
