package com.github.bednar.aap.processor;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jakub Bednář (22/11/2013 12:12)
 */
public class DTOTest
{
    private DTO dto;

    @Before
    public void before()
    {
        File outputDirectory = com.google.common.io.Files.createTempDir();

        dto = DTO.create(outputDirectory);
    }

    @Test
    public void dtoNotNull()
    {
        Assert.assertNotNull(dto);
    }

    @Test
    public void canCallGenerate()
    {
        dto.generate();
    }
}
