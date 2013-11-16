package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.ApiModelProperty;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * @author Jakub Bednář (16/11/2013 16:16)
 */
public class PropertyModelsSourceTransformer implements Function<TypeDeclaration, List<PropertyModel>>
{
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

    private class ApiModelPropertyVisitor extends VoidVisitorAdapter
    {
        List<PropertyModel> models = Lists.newArrayList();

        @Override
        public void visit(final FieldDeclaration n, final Object arg)
        {
            IsApiModelPropertyVisitor visitor = new IsApiModelPropertyVisitor();

            n.accept(visitor, null);

            if (visitor.getHasAnnotation())
            {
               models.add(new PropertyModel());
            }
        }

        @Nonnull
        private List<PropertyModel> getModels()
        {
            return models;
        }
    }

    private class IsApiModelPropertyVisitor extends VoidVisitorAdapter
    {
        private Boolean hasAnnotation = false;

        @Override
        public void visit(final NormalAnnotationExpr n, final Object arg)
        {
            if (n.getName().toString().equals(ApiModelProperty.class.getSimpleName()))
            {
                hasAnnotation = true;
            }
        }

        @Nonnull
        private Boolean getHasAnnotation()
        {
            return Boolean.TRUE.equals(hasAnnotation);
        }
    }
}
