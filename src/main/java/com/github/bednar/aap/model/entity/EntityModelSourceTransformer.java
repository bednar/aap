package com.github.bednar.aap.model.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

import com.github.bednar.aap.model.entity.parser.AnnotationExprTransformer;
import com.github.bednar.aap.model.entity.parser.PackageDeclarationTransformer;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.wordnik.swagger.annotations.ApiModel;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

/**
 * @author Jakub Bednář (16/11/2013 15:19)
 */
public class EntityModelSourceTransformer implements Function<File, EntityModel>
{
    @Nullable
    @Override
    public EntityModel apply(@SuppressWarnings("NullableProblems") @Nullable final File source)
    {
        if (source == null)
        {
            return null;
        }

        PackageDeclaration      packageDeclaration;
        List<ImportDeclaration> importsDeclaration;
        TypeDeclaration         typeDeclaration;
        try
        {
            CompilationUnit parse = JavaParser.parse(source);

            packageDeclaration  = parse.getPackage();
            importsDeclaration  = parse.getImports();
            typeDeclaration     = parse.getTypes().get(0);
        }
        catch (Exception e)
        {
            throw new EntityModelSourceTransformerException(e);
        }

        if (typeDeclaration.getAnnotations() == null)
        {
            return null;
        }

        AnnotationExpr apiModelAnnotation = Iterables.
                tryFind(typeDeclaration.getAnnotations(), new Predicate<AnnotationExpr>()
                {
                    @Override
                    public boolean apply(@Nullable final AnnotationExpr annotation)
                    {
                        return annotation != null && annotation.getName().getName().equals(ApiModel.class.getSimpleName());
                    }
                }).orNull();

        if (apiModelAnnotation == null)
        {
            return null;
        }

        String shortDescription = new AnnotationExprTransformer("value", "")
                .apply(apiModelAnnotation);

        List<PropertyModel> propertyModels = new PropertyModelsSourceTransformer(importsDeclaration)
                .apply(typeDeclaration);

        return new EntityModel()
                .setType(typeModel(packageDeclaration, typeDeclaration))
                .setShortDescription(shortDescription != null ? shortDescription : "")
                .setProperties(propertyModels);
    }

    @Nonnull
    private TypeModel typeModel(@Nonnull final PackageDeclaration packageDeclaration,
                                @Nonnull final TypeDeclaration typeDeclaration)
    {
        StringBuilder result = new StringBuilder();

        String packageName = new PackageDeclarationTransformer().apply(packageDeclaration);
        if (packageName != null)
        {
            result
                    .append(packageName)
                    .append(".");
        }
        result.append(typeDeclaration.getName());

        return new TypeModel(result.toString());
    }

    private class EntityModelSourceTransformerException extends RuntimeException
    {
        private EntityModelSourceTransformerException(final Throwable cause)
        {
            super(cause);
        }
    }
}
