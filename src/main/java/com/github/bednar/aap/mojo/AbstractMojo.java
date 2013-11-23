package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.collect.Iterables;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

/**
 * @author Jakub Bednář (06/11/2013 17:30)
 */
public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo
{
    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> classpathElements;

    @Component
    private RepositorySystem repositorySystem;

    private Reflections reflections;

    @Nonnull
    public final Reflections getReflections()
    {
        if (reflections == null)
        {
            buildClassPath();

            ConfigurationBuilder configuration = new ConfigurationBuilder();

            ClassRealm classRealm = classLoader();
            if (classRealm != null)
            {
                configuration.setUrls(classRealm.getURLs());
            }

            reflections = new Reflections(configuration);
        }

        return reflections;
    }

    private void buildClassPath()
    {
        //noinspection unchecked
        for (String sourceCompiledPath : Iterables.concat(classpathElements))
        {
            getLog().debug("Add Compiled path: " + sourceCompiledPath);

            try
            {
                Path path = Paths.get(sourceCompiledPath);

                if (Files.exists(path))
                {
                    ClassRealm classRealm = classLoader();
                    if (classRealm != null)
                    {
                        classRealm.addURL(path.toUri().toURL());
                    }
                    else
                    {
                        getLog().error("ClassRealm is null!");
                    }
                }
            }
            catch (MalformedURLException e)
            {
                throw new AbstractMojoException(e);
            }
        }
    }

    @Nullable
    private ClassRealm classLoader()
    {
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (ClassRealm.class.isAssignableFrom(classLoader.getClass()))
        {
            return (ClassRealm) classLoader;
        }
        else
        {
            return null;
        }
    }

    private class AbstractMojoException extends RuntimeException
    {
        private AbstractMojoException(final @Nonnull Throwable cause)
        {
            super(cause);
        }
    }
}
