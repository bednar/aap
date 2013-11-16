package com.github.bednar.aap.model.entity.parser;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * @author Jakub Bednář (16/11/2013 15:40)
 */
public class PackageDeclarationTransformer implements Function<PackageDeclaration, String>
{
    @Nullable
    @Override
    public String apply(@Nullable final PackageDeclaration declaration)
    {
        if (declaration == null)
        {
            return "";
        }

        PackageValueVisitor visitor = new PackageValueVisitor();

        declaration.accept(visitor, null);

        return visitor.getPackage();
    }

    private class PackageValueVisitor extends VoidVisitorAdapter
    {
        private String pkg;

        @Override
        public void visit(final PackageDeclaration n, final Object arg)
        {
            pkg = n.getName().toString();
        }

        @Nullable
        private String getPackage()
        {
            return pkg;
        }
    }
}
