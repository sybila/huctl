package cz.muni.fi.ctl.formula;


import cz.muni.fi.ctl.formula.operator.Operator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Formula {

    /** Get number of sub formulas. **/
    public int getSubFormulaCount();

    /** Get sub formula at specified index. **/
    public Formula getSubFormulaAt(int index);

    @NotNull
    public Collection<Formula> getSubFormulas();

    @NotNull
    public Operator getOperator();

}
