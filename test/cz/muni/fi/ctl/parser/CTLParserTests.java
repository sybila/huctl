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
        assertEquals(formula.getSubFormulaCount(), 1);
    }

    @Test
    public void existsNextTest() {
        Formula formula = CTLParser.parse("EX {True}");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), UnaryOperator.EXISTS_NEXT);
        assertEquals(formula.getSubFormulaCount(), 1);
    }

    @Test
    public void allFutureTest() {
        Formula formula = CTLParser.parse("AF {True}");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), UnaryOperator.ALL_FUTURE);
        assertEquals(formula.getSubFormulaCount(), 1);
    }

    @Test
    public void andTest() {
        Formula formula = CTLParser.parse("({True} && {False})");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.AND);
        assertEquals(formula.getSubFormulaCount(), 2);
    }

    @Test
    public void untilTest() {
        Formula formula = CTLParser.parse("E({True} U {False})");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(formula.getSubFormulaCount(), 2);
    }

    @Test
    public void complexTest() {
        Formula formula = CTLParser.parse("E(AF ({True} && !{False}) U EX E({True} U !({True} && {False})))");
        assertNotNull(formula);
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(formula.getSubFormulaCount(), 2);
        Formula af = formula.getSubFormulaAt(0);
        Formula ex = formula.getSubFormulaAt(1);
        assertEquals(af.getOperator(), UnaryOperator.ALL_FUTURE);
        assertEquals(af.getSubFormulaCount(), 1);
        assertEquals(ex.getOperator(), UnaryOperator.EXISTS_NEXT);
        assertEquals(ex.getSubFormulaCount(), 1);
        Formula and = af.getSubFormulaAt(0);
        assertEquals(and.getOperator(), BinaryOperator.AND);
        assertEquals(and.getSubFormulaCount(), 2);
        Formula prop = and.getSubFormulaAt(0);
        assertTrue(prop instanceof Proposition);
        Formula neg = and.getSubFormulaAt(1);
        assertEquals(neg.getOperator(), UnaryOperator.NEGATION);
        assertEquals(neg.getSubFormulaCount(), 1);
        prop = neg.getSubFormulaAt(0);
        assertTrue(prop instanceof Proposition);
        Formula until = ex.getSubFormulaAt(0);
        assertEquals(until.getOperator(), BinaryOperator.EXISTS_UNTIL);
        assertEquals(until.getSubFormulaCount(), 2);
        prop = until.getSubFormulaAt(0);
        assertTrue(prop instanceof Proposition);
        neg = until.getSubFormulaAt(1);
        assertEquals(neg.getOperator(), UnaryOperator.NEGATION);
        assertEquals(neg.getSubFormulaCount(), 1);
        and = neg.getSubFormulaAt(0);
        assertEquals(and.getOperator(), BinaryOperator.AND);
        assertEquals(and.getSubFormulaCount(), 2);
        prop = and.getSubFormulaAt(0);
        assertTrue(prop instanceof Proposition);
        prop = and.getSubFormulaAt(1);
        assertTrue(prop instanceof Proposition);

    }

}
