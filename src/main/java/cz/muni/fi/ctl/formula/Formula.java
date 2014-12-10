package cz.muni.fi.ctl.formula;


import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.Collection;

public interface Formula {

    /** Get number of sub formulas. **/
    public int getSubFormulaCount();

    /** Get sub formula at specified index. **/
    public Formula getSubFormulaAt(int index);

    public Collection<Formula> getSubFormulas();

    public Operator getOperator();

}
