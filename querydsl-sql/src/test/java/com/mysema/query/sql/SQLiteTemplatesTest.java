package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.template.NumberTemplate;

public class SQLiteTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLiteTemplates();
    }

    @Override
    public void Union() {
        NumberExpression<Integer> one = NumberTemplate.ONE;
        NumberExpression<Integer> two = NumberTemplate.TWO;
        NumberExpression<Integer> three = NumberTemplate.THREE;
        Path<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Union union = query.union(
                sq().unique(one.as(col1)),
                sq().unique(two),
                sq().unique(three));

        assertEquals(
                "select 1 as col1\n" +
                "union\n" +
                "select 2\n" +
                "union\n" +
                "select 3", union.toString());
    }

    @Test
    public void Precedence() {
        // ||
        // *    /    %
        int p1 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        // +    -
        int p2 = getPrecedence(Ops.ADD, Ops.SUB);
        // <<   >>   &    |
        // <    <=   >    >=
        int p3 = getPrecedence(Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        // =    ==   !=   <>   IS   IS NOT   IN   LIKE   GLOB   MATCH   REGEXP
        int p4 = getPrecedence(Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.IS_NULL, Ops.IS_NOT_NULL,
                Ops.IN, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.MATCHES);
        // AND
        int p5 = getPrecedence(Ops.AND);
        //  OR
        int p6 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
    }

}