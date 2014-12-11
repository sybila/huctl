
import cz.muni.fi.ctl.FormulaNormalizer;
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for FormulaNormalizer
 */
public class FormulaNormalizerTest {

    @Test
    public void propositionTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        assertEquals(Tautology.INSTANCE, normalizer.normalize(Tautology.INSTANCE));
        assertEquals(Contradiction.INSTANCE, normalizer.normalize(Contradiction.INSTANCE));
        Proposition proposition = new FloatProposition(34.5, "aaa", FloatProposition.Operator.GT_EQ);
        assertEquals(proposition, normalizer.normalize(proposition));
    }

    @Test
    public void unchangedTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula ex = new FormulaImpl(UnaryOperator.EXISTS_NEXT, Tautology.INSTANCE);
        assertEquals(ex, normalizer.normalize(ex));
        Formula eu = new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(eu, normalizer.normalize(eu));
        Formula au = new FormulaImpl(BinaryOperator.ALL_UNTIL, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(au, normalizer.normalize(au));
        Formula neg = new FormulaImpl(UnaryOperator.NEGATION, Tautology.INSTANCE);
        assertEquals(neg, normalizer.normalize(neg));
        Formula and = new FormulaImpl(BinaryOperator.AND, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(and, normalizer.normalize(and));
        Formula or = new FormulaImpl(BinaryOperator.OR, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(or, normalizer.normalize(or));
    }

    @Test
    public void allNextTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(UnaryOperator.NEGATION,
                            new FormulaImpl(UnaryOperator.EXISTS_NEXT,
                                    new FormulaImpl(UnaryOperator.NEGATION, Tautology.INSTANCE)));
        assertEquals(norm, normalizer.normalize(new FormulaImpl(UnaryOperator.ALL_NEXT, Tautology.INSTANCE)));
    }


    @Test
    public void allFutureTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(BinaryOperator.ALL_UNTIL, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(norm, normalizer.normalize(new FormulaImpl(UnaryOperator.ALL_FUTURE, Contradiction.INSTANCE)));
    }

    @Test
    public void existsFutureTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Tautology.INSTANCE, Contradiction.INSTANCE);
        assertEquals(norm, normalizer.normalize(new FormulaImpl(UnaryOperator.EXISTS_FUTURE, Contradiction.INSTANCE)));
    }

    @Test
    public void allGlobalTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(UnaryOperator.NEGATION,
                new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Tautology.INSTANCE,
                        new FormulaImpl(UnaryOperator.NEGATION, Contradiction.INSTANCE)));
        assertEquals(norm, normalizer.normalize(new FormulaImpl(UnaryOperator.ALL_GLOBAL, Contradiction.INSTANCE)));
    }

    @Test
    public void existsGlobalTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(UnaryOperator.NEGATION,
                new FormulaImpl(BinaryOperator.ALL_UNTIL, Tautology.INSTANCE,
                        new FormulaImpl(UnaryOperator.NEGATION, Contradiction.INSTANCE)));
        assertEquals(norm, normalizer.normalize(new FormulaImpl(UnaryOperator.EXISTS_GLOBAL, Contradiction.INSTANCE)));
    }

    @Test
    public void implicationTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(BinaryOperator.OR, new FormulaImpl(UnaryOperator.NEGATION, Contradiction.INSTANCE), Contradiction.INSTANCE);
        assertEquals(norm, normalizer.normalize(new FormulaImpl(BinaryOperator.IMPLICATION, Contradiction.INSTANCE, Contradiction.INSTANCE)));
    }

    @Test
    public void equivalenceTest() {
        FormulaNormalizer normalizer = new FormulaNormalizer();
        Formula norm = new FormulaImpl(BinaryOperator.OR,
                new FormulaImpl(BinaryOperator.AND, Contradiction.INSTANCE, Contradiction.INSTANCE),
                new FormulaImpl(BinaryOperator.AND,
                        new FormulaImpl(UnaryOperator.NEGATION, Contradiction.INSTANCE),
                        new FormulaImpl(UnaryOperator.NEGATION, Contradiction.INSTANCE)
                ));
        assertEquals(norm, normalizer.normalize(new FormulaImpl(BinaryOperator.EQUIVALENCE, Contradiction.INSTANCE, Contradiction.INSTANCE)));
    }
}
