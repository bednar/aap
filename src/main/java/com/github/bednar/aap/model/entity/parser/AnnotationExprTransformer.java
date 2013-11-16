package com.github.bednar.aap.model.entity.parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * @author Jakub Bednář (16/11/2013 15:28)
 */
public class AnnotationExprTransformer implements Function<AnnotationExpr, String>
{
    private String key = "value";

    private String defaultValue = null;

    public AnnotationExprTransformer()
    {
    }

    public AnnotationExprTransformer(@Nonnull final String key, @Nullable final String defaultValue)
    {
        super();

        //noinspection ConstantConditions
        Preconditions.checkArgument(key != null);
        Preconditions.checkArgument(!key.isEmpty());

        this.key            = key;
        this.defaultValue   = defaultValue;
    }

    @Override
    @Nullable
    public String apply(@Nullable final AnnotationExpr annotation)
    {
        if (annotation == null)
        {
            return defaultValue;
        }

        AnnotationValueVisitor visitor = new AnnotationValueVisitor();

        annotation.accept(visitor, null);

        return visitor.getValue() != null ? visitor.getValue() : defaultValue;
    }

    private class AnnotationValueVisitor extends VoidVisitorAdapter
    {
        private String value;

        @Override
        public void visit(final NormalAnnotationExpr n, final Object arg)
        {
            MemberValuePair memberValuePair = Iterables
                    .tryFind(n.getPairs(), new Predicate<MemberValuePair>()
                    {
                        @Override
                        public boolean apply(@Nullable final MemberValuePair input)
                        {
                            return input != null && input.getName().endsWith(key);
                        }
                    }).orNull();

            if (memberValuePair != null)
            {
                this.value = memberValuePair.getValue().toString();
            }
        }

        @Override
        public void visit(final SingleMemberAnnotationExpr n, final Object arg)
        {
            if ("value".equals(key))
            {
                this.value = n.getMemberValue().toString();
            }
        }

        @Nullable
        public String getValue()
        {
            return value != null ? value.replace("\"", "") : null;
        }
    }
}
