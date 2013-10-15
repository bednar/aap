package com.github.bednar.aap.processor.doc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.github.bednar.aap.model.ModelBuilder;
import com.github.bednar.aap.model.api.ApiModel;
import com.github.bednar.aap.model.entity.EntityModel;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
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
public final class Apiary
{
    private static final Logger LOG = LoggerFactory.getLogger(Apiary.class);

    private final File outputDirectory;

    private Template fileNameTemplate;
    private Template entityTemplate;
    private Template apiTemplate;
    private Template apiaryTemplate;

    private List<Class> apiClasses = Lists.newArrayList();
    private List<Class> entityClasses = Lists.newArrayList();

    private Apiary(final @Nonnull File outputDirectory)
    {
        LOG.info("[output-directory][{}]", outputDirectory.getAbsolutePath());

        this.outputDirectory = outputDirectory;
    }

    /**
     * @return new instance
     */
    @Nonnull
    public static Apiary create(final @Nonnull File outputDirectory)
    {
        return new Apiary(outputDirectory);
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
    public Apiary addApis(final @Nonnull Class... apiClasses)
    {
        Preconditions.checkNotNull(apiClasses);

        this.apiClasses = Lists.newArrayList(apiClasses);

        return this;
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
    public Apiary addEntities(final @Nonnull Class entityClasses)
    {
        Preconditions.checkNotNull(entityClasses);

        this.entityClasses = Lists.newArrayList(entityClasses);

        return this;
    }

    /**
     * Generate Apiary Blueprints documentation.
     *
     * @param appName name of app
     * @param baseURL base API url
     */
    public void generate(final @Nonnull String appName, final @Nonnull String baseURL)
    {
        initFreeMarker();

        List<String> evaluatedTemplates = Lists.newArrayList();

        for (EntityModel model : entityModels())
        {
            Path newFile = newFile(newFilePath(model.getType()));

            ImmutableMap<String, Object> data = ImmutableMap
                    .<String, Object>builder()
                    .put("model", model)
                    .build();

            String evaluateTemplate = evaluate(data, entityTemplate);

            writeFile(newFile, evaluateTemplate);

            evaluatedTemplates.add(evaluateTemplate);
        }

        for (ApiModel model : apiModels())
        {
            Path newFile = newFile(newFilePath(model.getType()));

            ImmutableMap<String, Object> data = ImmutableMap
                    .<String, Object>builder()
                    .put("model", model)
                    .build();

            String evaluateTemplate = evaluate(data, apiTemplate);

            writeFile(newFile, evaluateTemplate);

            evaluatedTemplates.add(evaluateTemplate);
        }

        Path newFile = newFile(newFilePath(Apiary.class));

        ImmutableMap<String, Object> data = ImmutableMap
                .<String, Object>builder()
                .put("templates", evaluatedTemplates)
                .put("appName", appName)
                .put("baseURL", baseURL)
                .build();

        String evaluateTemplate = evaluate(data, apiaryTemplate);

        writeFile(newFile, evaluateTemplate);
    }

    private void initFreeMarker()
    {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        try
        {
            fileNameTemplate = cfg.getTemplate("/doc/filename.ftl");
            entityTemplate = cfg.getTemplate("/doc/entity.ftl");
            apiTemplate = cfg.getTemplate("/doc/api.ftl");
            apiaryTemplate = cfg.getTemplate("/doc/apiary.ftl");
        }
        catch (Exception e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private Path newFilePath(final @Nonnull Class klass)
    {
        ImmutableMap<String, Object> data = ImmutableMap
                .<String, Object>builder()
                .put("class", klass)
                .build();

        String fileName = evaluate(data, fileNameTemplate);

        return Paths.get(outputDirectory.getAbsolutePath(), fileName);
    }

    @Nonnull
    private Path newFile(final @Nonnull Path fileToCreate)
    {
        try
        {
            Files.deleteIfExists(fileToCreate);

            return Files.createFile(fileToCreate);
        }
        catch (IOException e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private String evaluate(final @Nonnull Map<String, Object> data, final @Nonnull Template template)
    {
        StringWriter output = new StringWriter();
        try
        {
            template.process(data, output);
        }
        catch (Exception e)
        {
            throw new ApiaryException(e);
        }

        return output.toString();
    }

    private void writeFile(final @Nonnull Path newFile, final @Nonnull String evaluatedTemplate)
    {
        try
        {
            Files.write(newFile, evaluatedTemplate.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private List<ApiModel> apiModels()
    {
        return FluentIterable.from(apiClasses).transform(
                new Function<Class, ApiModel>()
                {
                    @Nullable
                    @Override
                    public ApiModel apply(final @SuppressWarnings("NullableProblems") @Nonnull Class klass)
                    {
                        return ModelBuilder.getInstance().getApiModel(klass);
                    }
                }).toList();
    }

    @Nonnull
    private List<EntityModel> entityModels()
    {
        return FluentIterable.from(entityClasses).transform(
                new Function<Class, EntityModel>()
                {
                    @Nullable
                    @Override
                    public EntityModel apply(final @SuppressWarnings("NullableProblems") @Nonnull Class klass)
                    {
                        return ModelBuilder.getInstance().getEntityModel(klass);
                    }
                }).toList();
    }

    private class ApiaryException extends RuntimeException
    {
        private ApiaryException(final Throwable cause)
        {
            super(cause);
        }
    }
}
