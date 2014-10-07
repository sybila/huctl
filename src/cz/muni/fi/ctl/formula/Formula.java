package cz.muni.fi.ctl.formula;


import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.List;

public interface Formula {

    public List<Formula> getSubFormulas();
    public Operator getOperator();

}
