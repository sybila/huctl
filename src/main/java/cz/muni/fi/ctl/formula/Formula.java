package cz.muni.fi.ctl.formula;


import cz.muni.fi.ctl.formula.operator.Operator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Formula {

    /** Get number of sub formulas. **/
    int getSubFormulaCount();

    /** Get sub formula at specified index. **/
    Formula getSubFormulaAt(int index);

    @NotNull
    Collection<Formula> getSubFormulas();

    @NotNull
    Operator getOperator();

}
