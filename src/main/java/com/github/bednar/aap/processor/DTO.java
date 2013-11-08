package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import java.io.File;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jakub Bednář (08/11/2013 09:04)
 */
public final class DTO extends AbstractProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(DTO.class);

    private final File outputDirectory;

    public DTO(@Nonnull final File outputDirectory)
    {
        this.outputDirectory = outputDirectory;

        createDirectory(outputDirectory);
    }

    /**
     * @return new instance
     */
    @Nonnull
    public static DTO create(@Nonnull final File outputDirectory)
    {
        Preconditions.checkNotNull(outputDirectory);

        return new DTO(outputDirectory);
    }
}
