package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

import com.github.bednar.aap.model.entity.parser.AnnotationExprTransformer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModelProperty;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import static com.github.bednar.aap.model.entity.EntityModelClassTransformer.propertyComparator;

/**
 * @author Jakub Bednář (16/11/2013 16:16)
 */
public class PropertyModelsSourceTransformer implements Function<TypeDeclaration, List<PropertyModel>>
{

    private final List<ImportDeclaration> importsDeclaration;

    public PropertyModelsSourceTransformer(@Nonnull final List<ImportDeclaration> importsDeclaration)
    {
        this.importsDeclaration = importsDeclaration;
    }

    @Nonnull
    @Override
    public List<PropertyModel> apply(@Nullable final TypeDeclaration typeDeclaration)
    {
        if (typeDeclaration == null)
        {
            return Lists.newArrayList();
        }

        ApiModelPropertyVisitor visitor = new ApiModelPropertyVisitor();
        typeDeclaration.accept(visitor, null);

        return visitor.getModels();
    }

    @Nonnull
    private PropertyModel createModel(@Nonnull final FieldDeclaration field,
                                      @Nonnull final GetAnnotationVisitor apiAnnotation,
                                      @Nonnull final GetAnnotationVisitor columnAnnotation,
                                      @Nonnull final GetAnnotationVisitor transientAnnotation)
    {
        String positionValue = new AnnotationExprTransformer("position", "0")
                .apply(apiAnnotation.getAnnotation());

        String shortDescription = new AnnotationExprTransformer("value", "")
                .apply(apiAnnotation.getAnnotation());

        String requiredValue = new AnnotationExprTransformer("nullable", "true")
                .apply(columnAnnotation.getAnnotation());

        String maxLengthValue = new AnnotationExprTransformer("length", "255")
                .apply(columnAnnotation.getAnnotation());

        String precisionValue = new AnnotationExprTransformer("precision", "0")
                .apply(columnAnnotation.getAnnotation());

        String scaleValue = new AnnotationExprTransformer("scale", "0")
                .apply(columnAnnotation.getAnnotation());

        //noinspection ConstantConditions
        return new PropertyModel()
                .setName(field.getVariables().get(0).getId().getName())
                .setShortDescription(shortDescription)
                .setPosition(Integer.valueOf(positionValue))
                .setType(createType(field.getType().toString()))
                .setIsTransient(transientAnnotation.getAnnotation() != null)
                .setRequired(!Boolean.valueOf(requiredValue))
                .setMaxLength(Integer.valueOf(maxLengthValue))
                .setPrecision(Integer.valueOf(precisionValue))
                .setScale(Integer.valueOf(scaleValue));
    }

    private class ApiModelPropertyVisitor extends VoidVisitorAdapter
    {
        List<PropertyModel> models = Lists.newArrayList();

        @Override
        public void visit(final FieldDeclaration field, final Object arg)
        {
            GetAnnotationVisitor apiVisitor = new GetAnnotationVisitor(ApiModelProperty.class);
            field.accept(apiVisitor, null);

            if (apiVisitor.getAnnotation() != null)
            {
                GetAnnotationVisitor columnVisitor = new GetAnnotationVisitor(Column.class);
                field.accept(columnVisitor, null);

                GetAnnotationVisitor transientVisitor = new GetAnnotationVisitor(Transient.class);
                field.accept(transientVisitor, null);

                models.add(createModel(field, apiVisitor, columnVisitor, transientVisitor));
            }
        }

        @Nonnull
        private List<PropertyModel> getModels()
        {
            return FluentIterable
                    .from(models)
                    .toSortedList(propertyComparator());
        }
    }

    @Nonnull
    private TypeModel createType(@Nonnull final String typeName)
    {
        ImportDeclaration declaration = Iterables.tryFind(importsDeclaration, new Predicate<ImportDeclaration>()
        {
            @Override
            public boolean apply(@Nullable final ImportDeclaration input)
            {
                return input != null && input.getName().toString().endsWith("." + typeName);
            }
        }).orNull();

        if (declaration != null)
        {
            return new TypeModel(declaration.getName().toString());
        }
        else
        {
            try
            {
                return new TypeModel(Class.forName("java.lang." + typeName).getCanonicalName());
            }
            catch (ClassNotFoundException e)
            {
                throw new PropertyModelsSourceTransformerException(e);
            }
        }
    }

    private class GetAnnotationVisitor extends VoidVisitorAdapter
    {
        private AnnotationExpr annotation = null;

        private final Class annotationType;

        private GetAnnotationVisitor(final Class annotationType)
        {
            this.annotationType = annotationType;
        }

        @Override
        public void visit(final NormalAnnotationExpr n, final Object arg)
        {
            if (n.getName().toString().equals(annotationType.getSimpleName()))
            {
                annotation = n;
            }
        }

        @Override
        public void visit(final MarkerAnnotationExpr n, final Object arg)
        {
            if (n.getName().toString().equals(annotationType.getSimpleName()))
            {
                annotation = n;
            }
        }

        @Nullable
        private AnnotationExpr getAnnotation()
        {
            return annotation;
        }
    }

    private class PropertyModelsSourceTransformerException extends RuntimeException
    {
        public PropertyModelsSourceTransformerException(final Throwable cause)
        {
            super(cause);
        }
    }
}
