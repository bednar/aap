package com.github.bednar.aap.processor.doc;

import javax.annotation.Nonnull;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Create Apiary Documentation.
 * <p/>
 * See example: <a href="http://docs.teamoons.apiary.io">Teamoons.com</a>
 *
 * @author Jakub Bednář (07/10/2013 7:47 PM)
 */
public final class Apiary
{
    private Configuration cfg;

    private Template fileNameTemplate;

    private List<Class> apiClasses = Lists.newArrayList();
    private List<Class> entityClasses = Lists.newArrayList();

    private Apiary()
    {
    }

    /**
     * @return new instance
     */
    @Nonnull
    public static Apiary create()
    {
        return new Apiary();
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
     */
    public void generate()
    {
        initFreeMarker();

        for (Class klass : apiClasses)
        {
            System.out.println("fileName(klass) = " + fileName(klass));
        }

        for (Class klass : entityClasses)
        {
            System.out.println("fileName(klass) = " + fileName(klass));
        }
    }

    private void initFreeMarker()
    {
        cfg = new Configuration();
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        try
        {
            fileNameTemplate = cfg.getTemplate("/doc/filename.ftl");
        }
        catch (Exception e)
        {
            throw new ApiaryException(e);
        }
    }

    @Nonnull
    private String fileName(final @Nonnull Class klass)
    {
        ImmutableMap<String, Object> data = ImmutableMap
                .<String, Object>builder()
                .put("class", klass)
                .build();

        return process(data, fileNameTemplate);
    }

    @Nonnull
    private String process(final @Nonnull Map<String, Object> data, final @Nonnull Template template)
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

    private class ApiaryException extends RuntimeException
    {
        private ApiaryException(final Throwable cause)
        {
            super(cause);
        }
    }
}
