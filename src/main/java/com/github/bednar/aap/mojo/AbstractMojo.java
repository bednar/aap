package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

/**
 * @author Jakub Bednář (06/11/2013 17:30)
 */
public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo
{
    private Reflections reflections;

    @Nonnull
    @SafeVarargs
    public final Reflections getReflections(@Nonnull final List<String>... sourceCompiledPaths)
    {
        Preconditions.checkNotNull(sourceCompiledPaths);

        if (reflections == null)
        {
            URLClassLoader urlClassLoader = buildClassPath(sourceCompiledPaths);

            reflections = new Reflections(
                    new ConfigurationBuilder()
                            .setUrls(urlClassLoader.getURLs())
                            .addClassLoader(urlClassLoader));
        }

        return reflections;
    }

    @Nonnull
    @SafeVarargs
    private final URLClassLoader buildClassPath(@Nonnull final List<String>... sourceCompiledPaths)
    {
        //Add exist compiled path
        URL[] urls = FluentIterable.from(Iterables.concat(sourceCompiledPaths))
                .filter(new Predicate<String>()
                {
                    @Override
                    public boolean apply(@Nullable final String path)
                    {
                        return path != null && Files.exists(Paths.get(path));
                    }
                })
                .transform(new Function<String, URL>()
                {
                    @Override
                    public URL apply(@SuppressWarnings("NullableProblems") @Nonnull final String path)
                    {
                        try
                        {
                            return Paths.get(path).toUri().toURL();
                        }
                        catch (MalformedURLException e)
                        {
                            throw new AbstractMojoException(e);
                        }
                    }
                }).toArray(URL.class);

        return URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader());
    }

    private class AbstractMojoException extends RuntimeException
    {
        private AbstractMojoException(final @Nonnull Throwable cause)
        {
            super(cause);
        }
    }
}
