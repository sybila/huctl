import cz.muni.fi.ctl.FormulaParser;
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for FormulaParser
 */
public class FormulaParserTest {

    private static final float TOLERANCE = 0.0001f;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void propositionTest() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("#define a B > 3.0 #property a");
        assertThat(formula, instanceOf(FloatProposition.class));
        FloatProposition proposition = (FloatProposition) formula;
        assertEquals(proposition.getThreshold(), 3.0, TOLERANCE);
        assertEquals(proposition.getVariable(), "B");
        //long names
        formula = parser.parse("#define someLongName ANOTHERlongNAME > 1234.5678 #property someLongName");
        assertThat(formula, instanceOf(FloatProposition.class));
        proposition = (FloatProposition) formula;
        assertEquals(proposition.getThreshold(), 1234.5678, TOLERANCE);
        assertEquals(proposition.getVariable(), "ANOTHERlongNAME");
        //negative numbers
        formula = parser.parse("#define a B > -23.4 #property a");
        assertThat(formula, instanceOf(FloatProposition.class));
        proposition = (FloatProposition) formula;
        assertEquals(proposition.getThreshold(), -23.4, TOLERANCE);
        assertEquals(proposition.getVariable(), "B");
        //unused propositions
        //this should produce an exception
        exception.expect(IllegalArgumentException.class);
        parser.parse("#define a B > 3.0 #define c C <= 23.4 #define z Z > 4.0 #property c");
        //proposition redefinition
        exception.expect(IllegalArgumentException.class);
        parser.parse("#define a B > 3.0 #define a C <= 23.4 #define z Z > 4.0 #property c");
        //unknown proposition
        exception.expect(IllegalArgumentException.class);
        parser.parse("#define a B > 3.0 #define a C <= 23.4 #define z Z > 4.0 #property D");
    }

    @Test
    public void booleanLiteralsTest() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("#property True");
        assertThat(formula, instanceOf(Tautology.class));
        formula = parser.parse("#property False");
        assertThat(formula, instanceOf(Contradiction.class));
    }

    @Test
    public void unaryTemporalOperatorTest() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("#property EX False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.EXISTS_NEXT);
        formula = parser.parse("#property AX False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.ALL_NEXT);
        formula = parser.parse("#property EF False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.EXISTS_FUTURE);
        formula = parser.parse("#property AF False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.ALL_FUTURE);
        formula = parser.parse("#property EG False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.EXISTS_GLOBAL);
        formula = parser.parse("#property AG False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.ALL_GLOBAL);
    }

    @Test
    public void binaryTemporalOperator() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("#property E (True U False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        formula = parser.parse("#property A (True U False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.ALL_UNTIL);
    }

    @Test
    public void booleanOperatorTest() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("#property ! False");
        assertEquals(formula.getSubFormulaCount(), 1);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(UnaryOperator.class));
        assertEquals(formula.getOperator(), UnaryOperator.NEGATION);
        formula = parser.parse("#property (True && False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.AND);
        formula = parser.parse("#property (True || False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.OR);
        formula = parser.parse("#property (True => False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.IMPLICATION);
        formula = parser.parse("#property (True <=> False)");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertThat(formula.getSubFormulaAt(0), instanceOf(Tautology.class));
        assertThat(formula.getSubFormulaAt(1), instanceOf(Contradiction.class));
        assertThat(formula.getOperator(), instanceOf(BinaryOperator.class));
        assertEquals(formula.getOperator(), BinaryOperator.EQUIVALENCE);
    }

    @Test
    public void complexTest() {
        FormulaParser parser = new FormulaParser();
        Formula formula = parser.parse("" +
                "#define first XY < 2.7 \n" +
                "#define second aBx >= -0.01 \t \t \n" +
                "#define Third zZ != 41234.45 \n" +
                "#property E ( AF EX AG (first => True) U ( EF Third <=> A ( False U second ) ) ) ");
        assertEquals(formula.getSubFormulaCount(), 2);
        assertEquals(formula.getOperator(), BinaryOperator.EXISTS_UNTIL);
        Formula af = formula.getSubFormulaAt(0);
        assertEquals(af.getSubFormulaCount(), 1);
        assertEquals(af.getOperator(), UnaryOperator.ALL_FUTURE);
        Formula ex = af.getSubFormulaAt(0);
        assertEquals(ex.getSubFormulaCount(), 1);
        assertEquals(ex.getOperator(), UnaryOperator.EXISTS_NEXT);
        Formula ag = ex.getSubFormulaAt(0);
        assertEquals(ag.getSubFormulaCount(), 1);
        assertEquals(ag.getOperator(), UnaryOperator.ALL_GLOBAL);
        Formula impl = ag.getSubFormulaAt(0);
        assertEquals(impl.getSubFormulaCount(), 2);
        assertEquals(impl.getOperator(), BinaryOperator.IMPLICATION);
        assertThat(impl.getSubFormulaAt(0), instanceOf(FloatProposition.class));
        assertThat(impl.getSubFormulaAt(1), instanceOf(Tautology.class));
        FloatProposition first = (FloatProposition) impl.getSubFormulaAt(0);
        assertEquals(first.getVariable(), "XY");
        assertEquals(first.getThreshold(), 2.7, TOLERANCE);
        Formula equiv = formula.getSubFormulaAt(1);
        assertEquals(equiv.getSubFormulaCount(), 2);
        assertEquals(equiv.getOperator(), BinaryOperator.EQUIVALENCE);
        Formula ef = equiv.getSubFormulaAt(0);
        assertEquals(ef.getSubFormulaCount(), 1);
        assertEquals(ef.getOperator(), UnaryOperator.EXISTS_FUTURE);
        assertThat(ef.getSubFormulaAt(0), instanceOf(FloatProposition.class));
        FloatProposition third = (FloatProposition) ef.getSubFormulaAt(0);
        assertEquals(third.getVariable(), "zZ");
        assertEquals(third.getThreshold(), 41234.45, TOLERANCE);
        Formula au = equiv.getSubFormulaAt(1);
        assertEquals(au.getSubFormulaCount(), 2);
        assertEquals(au.getOperator(), BinaryOperator.ALL_UNTIL);
        assertThat(au.getSubFormulaAt(0), instanceOf(Contradiction.class));
        assertThat(au.getSubFormulaAt(1), instanceOf(FloatProposition.class));
        FloatProposition second = (FloatProposition) au.getSubFormulaAt(1);
        assertEquals(second.getVariable(), "aBx");
        assertEquals(second.getThreshold(), -0.01, TOLERANCE);
    }

}
