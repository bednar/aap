package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.github.bednar.aap.model.ModelBuilder;
import com.github.bednar.aap.model.api.ApiModel;
import com.github.bednar.aap.model.entity.EntityModel;
import com.github.bednar.aap.model.entity.TypeModel;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Apiary Documentation.
 * <p/>
 * See example: <a href="http://docs.teamoons.apiary.io">Teamoons.com</a>
 *
 * @author Jakub Bednář (07/10/2013 7:47 PM)
 */
public final class Apiary extends AbstractProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(Apiary.class);

    private final File outputDirectory;

    private Template fileNameTemplate;
    private Template entityTemplate;
    private Template apiTemplate;
    private Template apiaryTemplate;

    private List<Class> apiClasses = Lists.newArrayList();
    private List<Class> entityClasses = Lists.newArrayList();

    private Apiary(@Nonnull final File outputDirectory)
    {
        LOG.info("[output-directory][{}]", outputDirectory.getAbsolutePath());

        this.outputDirectory = outputDirectory;

        createDirectory(outputDirectory);
    }

    /**
     * @return new instance
     */
    @Nonnull
    public static Apiary create(@Nonnull final File outputDirectory)
    {
        Preconditions.checkNotNull(outputDirectory);

        return new Apiary(outputDirectory);
    }

    /**
     * @param apiClasses apis
     *
     * @return this
     *
     * @see #addApis(java.util.Collection)
     */
    @Nonnull
    public Apiary addApis(@Nonnull final Class<?>... apiClasses)
    {
        Preconditions.checkNotNull(apiClasses);

        return addApis(Lists.newArrayList(apiClasses));
    }

    /**
     * Add classes which are JAX-RS Resource documented by annotations.
     * <p/>
     * Example com.github.bednar.aap.example.PubApi in test sources.
     *
     * @param apiClasses apis
     *
     * @return this
     *
     * @see com.wordnik.swagger.annotations.Api
     * @see javax.ws.rs.Path
     */
    @Nonnull
    public Apiary addApis(@Nonnull final Collection<Class<?>> apiClasses)
    {
        Preconditions.checkNotNull(apiClasses);

        this.apiClasses.addAll(apiClasses);

        return this;
    }

    /**
     * @param entityClasses entities
     *
     * @return this
     *
     * @see #addEntities(java.util.Collection)
     */
    @Nonnull
    public Apiary addEntities(@Nonnull final Class<?>... entityClasses)
    {
        Preconditions.checkNotNull(entityClasses);

        return addEntities(Lists.newArrayList(entityClasses));
    }

    /**
     * Add classes which are domain object classes (documented by annotations) of JAX-RS resource .
     * <p/>
     * Example: com.github.bednar.aap.example.PubApi.Meal in test sources.
     *
     * @param entityClasses entities
     *
     * @return this
     */
    @Nonnull
    public Apiary addEntities(@Nonnull final Collection<Class<?>> entityClasses)
    {
        Preconditions.checkNotNull(entityClasses);

        this.entityClasses.addAll(entityClasses);

        return this;
    }

    /**
     * Blueprint name: "Apiary.md".
     *
     * @see #generate(String, String, java.io.File, String)
     */
    public void generate(@Nonnull final String appName,
                         @Nonnull final String baseURL,
                         @Nullable final File description)
    {
        generate(appName, baseURL, description, "Apiary.md");
    }

    /**
     * Generate Apiary Blueprints documentation.
     *
     * @param appName       name of app [required]
     * @param baseURL       base API url [required]
     * @param description   file with description of app
     * @param blueprintName Name of Apiary.io Blueprint file
     */
    public void generate(@Nonnull final String appName,
                         @Nonnull final String baseURL,
                         @Nullable final File description,
                         @Nonnull final String blueprintName)
    {
        Preconditions.checkNotNull(appName);
        Preconditions.checkNotNull(baseURL);
        Preconditions.checkNotNull(blueprintName);

        initFreeMarker();

        List<String> evaluatedTemplates = Lists.newArrayList();

        for (EntityModel model : entityModels(entityClasses))
        {
            String evaluateTemplate = evaluate(entityTemplate, "model", model);

            createFile(model.getType(), evaluateTemplate);

            evaluatedTemplates.add(evaluateTemplate);
        }

        for (ApiModel model : apiModels())
        {
            String evaluateTemplate = evaluate(apiTemplate, "model", model);

            createFile(model.getType(), evaluateTemplate);

            evaluatedTemplates.add(evaluateTemplate);
        }

        String evaluateTemplate = evaluate(apiaryTemplate,
                "templates",    evaluatedTemplates,
                "appName",      appName,
                "baseURL",      baseURL,
                "description",  readFile(description));

        createFileWithName(blueprintName, evaluateTemplate);
    }

    private void initFreeMarker()
    {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        try
        {
            fileNameTemplate    = cfg.getTemplate("/doc/filename.ftl");
            entityTemplate      = cfg.getTemplate("/doc/entity.ftl");
            apiTemplate         = cfg.getTemplate("/doc/api.ftl");
            apiaryTemplate      = cfg.getTemplate("/doc/apiary.ftl");
        }
        catch (Exception e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private String evaluate(@Nonnull final Template template, @Nonnull final Object... keyValues)
    {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();

        for (int i = 0; i < keyValues.length; i += 2)
        {
            Object key = keyValues[i];
            Object value = keyValues[i + 1];

            map.put(key.toString(), value);
        }

        StringWriter output = new StringWriter();
        try
        {
            template.process(map.build(), output);
        }
        catch (Exception e)
        {
            throw new ApiaryException(e);
        }

        return output.toString();
    }

    private void createFile(@Nonnull final Class type, @Nonnull final String evaluatedTemplate)
    {
        createFile(type.getSimpleName(), evaluatedTemplate);
    }

    private void createFile(@Nonnull final TypeModel type, @Nonnull final String evaluatedTemplate)
    {
        createFile(type.getSimpleName(), evaluatedTemplate);
    }

    private void createFile(@Nonnull final String type, @Nonnull final String evaluatedTemplate)
    {
        Path path = newFilePath(type);

        createFile(path, evaluatedTemplate);
    }

    private void createFileWithName(@Nonnull final String fileName, @Nonnull final String evaluatedTemplate)
    {
        Path path = newFilePathWithName(fileName);

        createFile(path, evaluatedTemplate);
    }

    private void createFile(@Nonnull final Path filePath, @Nonnull final String evaluatedTemplate)
    {
        try
        {
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);
            Files.write(filePath, evaluatedTemplate.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private Path newFilePath(@Nonnull final String simpleName)
    {
        String fileName = evaluate(fileNameTemplate, "simpleName", simpleName);

        return newFilePathWithName(fileName);
    }

    @Nonnull
    private Path newFilePathWithName(@Nonnull final String fileName)
    {
        return Paths.get(outputDirectory.getAbsolutePath(), fileName);
    }

    @Nonnull
    private String readFile(@Nullable final File file)
    {
        if (file == null || !file.exists())
        {
            return "";
        }
        try
        {
            return com.google.common.io.Files.toString(file, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private List<ApiModel> apiModels()
    {
        return FluentIterable.from(apiClasses)
                .transform(
                        new Function<Class, ApiModel>()
                        {
                            @Nullable
                            @Override
                            public ApiModel apply(final @SuppressWarnings("NullableProblems") @Nonnull Class klass)
                            {
                                Preconditions.checkNotNull(klass);

                                return ModelBuilder.getInstance().getApiModel(klass);
                            }
                        })
                .toSortedList(
                        new Comparator<ApiModel>()
                        {
                            @Override
                            public int compare(final ApiModel model1, final ApiModel model2)
                            {
                                return ComparisonChain.start()
                                        .compare(model1.getPosition(), model2.getPosition())
                                        .result();
                            }
                        });
    }

    private static class ApiaryException extends RuntimeException
    {
        private ApiaryException(final Throwable cause)
        {
            super(cause);
        }
    }
}
