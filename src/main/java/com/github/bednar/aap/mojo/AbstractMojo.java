package com.github.bednar.aap.mojo;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
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
    @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
    private ArtifactRepository localRepository;

    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> sourceCompiledPaths;

    @Parameter(defaultValue = "${project.dependencyArtifacts}", required = true, readonly = true)
    private Set<Artifact> dependencyArtifacts;

    @Component
    private RepositorySystem repositorySystem;

    private Reflections reflections;

    @Nonnull
    public final Reflections getReflections()
    {
        if (reflections == null)
        {
            buildClassPath();

            reflections = new Reflections(
                    new ConfigurationBuilder()
                            .setUrls(classLoader().getURLs())
            );
        }

        return reflections;
    }

    protected final void buildClassPath()
    {
        buildSourceClassPath();
        buildDependencyClassPath();
    }

    private void buildSourceClassPath()
    {
        //noinspection unchecked
        for (String sourceCompiledPath : Iterables.concat(sourceCompiledPaths))
        {
            try
            {
                Path path = Paths.get(sourceCompiledPath);

                if (Files.exists(path))
                {
                    addURLToClassPath(path.toUri().toURL());
                }
            }
            catch (MalformedURLException e)
            {
                throw new AbstractMojoException(e);
            }
        }
    }

    private void buildDependencyClassPath()
    {
        for (Artifact dependencyArtifact : dependencyArtifacts)
        {
            //search transitive dependency
            ArtifactResolutionRequest request = new ArtifactResolutionRequest()
                    .setArtifact(dependencyArtifact)
                    .setResolveTransitively(true)
                    .setLocalRepository(localRepository);

            ArtifactResolutionResult resolve = repositorySystem.resolve(request);

            //add artifact and dependency
            for (Artifact transitive : resolve.getArtifacts())
            {
                addArtifactToClassPath(transitive);
            }
        }
    }

    private void addArtifactToClassPath(@Nonnull final Artifact artifact)
    {
        Preconditions.checkNotNull(artifact);

        try
        {
            URL url = artifact.getFile().toURI().toURL();

            addURLToClassPath(url);
        }
        catch (MalformedURLException e)
        {
            throw new AbstractMojoException(e);
        }
    }

    private void addURLToClassPath(@Nonnull final URL url)
    {
        Preconditions.checkNotNull(url);

        ClassRealm classLoader = classLoader();

        classLoader.addURL(url);
    }

    @Nonnull
    private ClassRealm classLoader()
    {
        return (ClassRealm) this.getClass().getClassLoader();
    }

    private class AbstractMojoException extends RuntimeException
    {
        private AbstractMojoException(final @Nonnull Throwable cause)
        {
            super(cause);
        }
    }
}
