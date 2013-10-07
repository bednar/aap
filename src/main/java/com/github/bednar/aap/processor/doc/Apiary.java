package com.github.bednar.aap.processor.doc;

import javax.annotation.Nonnull;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Create Apiary Documentation.
 * <p/>
 * See example: <a href="http://docs.teamoons.apiary.io">Teamoons.com</a>
 *
 * @author Jakub Bednář (07/10/2013 7:47 PM)
 */
public final class Apiary
{
    private Apiary()
    {
    }

    private List<Class> apiClasses      = Lists.newArrayList();
    private List<Class> entityClasses   = Lists.newArrayList();

    /**
     * @return new instance
     */
    @Nonnull
    public static Apiary create()
    {
        return new Apiary();
    }

    /**
     * Add classes which are JAX-RS Resource with documentation annotations.
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
     * Add classes which are domain object classes (with documentation annotations) of JAX-RS resource .
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
    }
}
