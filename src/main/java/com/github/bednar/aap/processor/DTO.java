package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collection;
import java.util.List;

import com.github.bednar.aap.model.entity.EntityModel;
import com.github.bednar.aap.model.entity.PropertyModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jakub Bednář (08/11/2013 09:04)
 */
public final class DTO extends AbstractProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(DTO.class);

    private final File outputDirectory;

    private List<Class> entityClasses = Lists.newArrayList();

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
    public DTO addEntities(@Nonnull final Collection<Class<?>> entityClasses)
    {
        Preconditions.checkNotNull(entityClasses);

        this.entityClasses.addAll(entityClasses);

        return this;
    }

    /**
     * Generate DTO Java classes.
     */
    public void generate()
    {
        for (EntityModel model : entityModels(entityClasses))
        {
            generateDTO(model);
        }
    }

    private void generateDTO(@Nonnull final EntityModel model)
    {
        Preconditions.checkNotNull(model);

        String dtoClassName = model.getType().getCanonicalName() + "DTO";

        LOG.info("[generate-class][{}]", dtoClassName);

        JCodeModel codeModel = new JCodeModel();
        try
        {
            JDefinedClass definedClass = codeModel._class(dtoClassName);

            for (PropertyModel propertyModel : model.getProperties())
            {
                generateProperty(propertyModel, definedClass, codeModel);
            }

            codeModel.build(outputDirectory);
        }
        catch (Exception e)
        {
            throw new DTOException(e);
        }

        LOG.info("[generate-class][done]");
    }

    private void generateProperty(@Nonnull final PropertyModel propertyModel,
                                  @Nonnull final JDefinedClass definedClass,
                                  @Nonnull final JCodeModel codeModel)
    {
        String fieldName    = propertyModel.getName();
        JClass type         = codeModel.directClass(propertyModel.getType().getCanonicalName());

        JFieldVar field = definedClass.field(JMod.PRIVATE, type, fieldName);

        //Getter Method
        String getterName = "get" + WordUtils.capitalize(fieldName);
        JMethod getterMethod = definedClass.method(JMod.PUBLIC, type, getterName);
        getterMethod
                .body()
                ._return(field);

        //Setter Method
        String setterName = "set" + WordUtils.capitalize(fieldName);
        JMethod setterMethod = definedClass.method(JMod.PUBLIC, Void.TYPE, setterName);
        setterMethod.param(type, fieldName);
        setterMethod
                .body()
                .assign(JExpr._this().ref(fieldName), JExpr.ref(fieldName));
    }

    private static class DTOException extends RuntimeException
    {
        private DTOException(@Nonnull final Throwable cause)
        {
            super(cause);
        }
    }
}
