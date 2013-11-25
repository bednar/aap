package com.github.bednar.aap.processor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.PrintStream;
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
import org.apache.maven.plugin.logging.Log;

/**
 * @author Jakub Bednář (08/11/2013 09:04)
 */
public final class DTO extends AbstractProcessor
{
    private final File outputDirectory;

    private List<EntityModel> entityModels = Lists.newArrayList();

    private DTO(@Nonnull final File outputDirectory, @Nonnull final Log log)
    {
        super(log);

        this.outputDirectory = outputDirectory;

        createDirectory(outputDirectory);
    }

    /**
     * @return new instance
     */
    @Nonnull
    public static DTO create(@Nonnull final File outputDirectory, @Nonnull final Log log)
    {
        Preconditions.checkNotNull(outputDirectory);
        Preconditions.checkNotNull(log);

        return new DTO(outputDirectory, log);
    }

    /**
     * Add models which are generated from domain object classes (documented by annotations) of JAX-RS resource .
     * <p/>
     * Example: com.github.bednar.aap.example.PubApi.Meal in test sources.
     *
     * @param entityModels entities
     *
     * @return this
     */
    @Nonnull
    public DTO addEntityModels(@Nonnull final Collection<EntityModel> entityModels)
    {
        Preconditions.checkNotNull(entityModels);

        this.entityModels.addAll(entityModels);

        return this;
    }

    /**
     * Generate DTO Java classes.
     */
    public void generate()
    {
        for (EntityModel model : entityModels)
        {
            generateDTO(model);
        }
    }

    private void generateDTO(@Nonnull final EntityModel model)
    {
        Preconditions.checkNotNull(model);

        String dtoClassName = model.getType().getCanonicalName() + "DTO";

        log.info("[generate-class][" + dtoClassName + "]");

        JCodeModel codeModel = new JCodeModel();
        try
        {
            JDefinedClass definedClass = codeModel._class(dtoClassName);

            for (PropertyModel propertyModel : model.getProperties())
            {
                generateProperty(propertyModel, definedClass, codeModel);
            }

            LogPrintStream logStream = new LogPrintStream();
            codeModel.build(outputDirectory, new PrintStream(logStream));

            log.info("[generate-message][" + logStream.message + "]");
        }
        catch (Exception e)
        {
            throw new DTOException(e);
        }

        log.info("[generate-class][done]");
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
