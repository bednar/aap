package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import com.github.bednar.aap.processor.doc.Apiary;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

/**
 * Generate Apiary Blueprint.
 *
 * @author Jakub Bednář (15/10/2013 4:24 PM)
 */
@Mojo(name = "apiary")
public class ApiaryMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> sourceCompiledPaths;

    /**
     * Documentation output
     */
    @Parameter(defaultValue = "${project.build.directory}/generated/apiary")
    private File apiaryOutput;

    /**
     * Name of Application
     */
    @Parameter(defaultValue = "Demo Application")
    private String appName;

    /**
     * File with Description (support <a href="http://daringfireball.net/projects/markdown/syntax">Markdown</a>) of Application
     */
    @Parameter(defaultValue = "${project.basedir}/description.md")
    private File appDescription;

    /**
     * API Base URL
     */
    @Parameter(defaultValue = "http://demoaap.apiary.io")
    private String apiBaseURL;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info(String.format("[apiary-cfg][appName:%s][apiBaseURL:%s][appDescription:%s]", appName, apiBaseURL, appDescription));

        URLClassLoader urlClassLoader = buildClassPath();

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(urlClassLoader.getURLs())
                        .addClassLoader(urlClassLoader));

        Collection<Class<?>> apis       = reflections.getTypesAnnotatedWith(Api.class);
        Collection<Class<?>> entities   = reflections.getTypesAnnotatedWith(ApiModel.class);

        Apiary
                .create(apiaryOutput)
                .addApis(apis)
                .addEntities(entities)
                .generate(appName, apiBaseURL, appDescription);
    }

    @Nonnull
    private URLClassLoader buildClassPath()
    {
        //Add exist compiled path
        URL[] urls = FluentIterable.from(sourceCompiledPaths)
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
                            throw new GenerateMojoException(e);
                        }
                    }
                }).toArray(URL.class);

        return URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader());
    }

    private class GenerateMojoException extends RuntimeException
    {
        private GenerateMojoException(final @Nonnull Throwable cause)
        {
            super(cause);
        }
    }
}
