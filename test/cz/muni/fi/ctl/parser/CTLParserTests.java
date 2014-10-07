package cz.muni.fi.ctl.parser;


import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import org.junit.Test;

import static org.junit.Assert.*;

public class CTLParserTests {

    @Test
    public void propositionTest() {
        Formula formula = CTLParser.parse("{True}");
        assertNotNull(formula);
        assertTrue(formula instanceof Proposition);
    }

    @Test
    public void negationTest() {
        Formula formula = CTLParser.parse("!{True}");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), UnaryOperator.NEGATION);
        assertEquals(formula.getSubFormulas().size(), 1);
    }

    @Test
    public void existsNextTest() {
        Formula formula = CTLParser.parse("EX {True}");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), UnaryOperator.EXISTS_NEXT);
        assertEquals(formula.getSubFormulas().size(), 1);
    }

    @Test
    public void allFutureTest() {
        Formula formula = CTLParser.parse("AF {True}");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), UnaryOperator.ALL_FUTURE);
        assertEquals(formula.getSubFormulas().size(), 1);
    }

    @Test
    public void andTest() {
        Formula formula = CTLParser.parse("({True} && {False})");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.AND);
        assertEquals(formula.getSubFormulas().size(), 2);
    }

    @Test
    public void untilTest() {
        Formula formula = CTLParser.parse("E({True} U {False})");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(formula.getSubFormulas().size(), 2);
    }

    @Test
    public void complexTest() {
        Formula formula = CTLParser.parse("E(AF ({True} && !{False}) U EX E({True} U !({True} && {False})))");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(formula.getSubFormulas().size(), 2);
        Formula af = formula.getSubFormulas().get(0);
        Formula ex = formula.getSubFormulas().get(1);
        assertEquals(af.getOperator(), UnaryOperator.ALL_FUTURE);
        assertEquals(af.getSubFormulas().size(), 1);
        assertEquals(ex.getOperator(), UnaryOperator.EXISTS_NEXT);
        assertEquals(ex.getSubFormulas().size(), 1);
        Formula and = af.getSubFormulas().get(0);
        assertEquals(and.getOperator(), BinaryOperator.AND);
        assertEquals(and.getSubFormulas().size(), 2);
        Formula prop = and.getSubFormulas().get(0);
        assertTrue(prop instanceof Proposition);
        Formula neg = and.getSubFormulas().get(1);
        assertEquals(neg.getOperator(), UnaryOperator.NEGATION);
        assertEquals(neg.getSubFormulas().size(), 1);
        prop = neg.getSubFormulas().get(0);
        assertTrue(prop instanceof Proposition);
        Formula until = ex.getSubFormulas().get(0);
        assertEquals(until.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(until.getSubFormulas().size(), 2);
        prop = until.getSubFormulas().get(0);
        assertTrue(prop instanceof Proposition);
        neg = until.getSubFormulas().get(1);
        assertEquals(neg.getOperator(), UnaryOperator.NEGATION);
        assertEquals(neg.getSubFormulas().size(), 1);
        and = neg.getSubFormulas().get(0);
        assertEquals(and.getOperator(), BinaryOperator.AND);
        assertEquals(and.getSubFormulas().size(), 2);
        prop = and.getSubFormulas().get(0);
        assertTrue(prop instanceof Proposition);
        prop = and.getSubFormulas().get(1);
        assertTrue(prop instanceof Proposition);

    }

}
