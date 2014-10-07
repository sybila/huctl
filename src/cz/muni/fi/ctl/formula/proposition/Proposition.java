package cz.muni.fi.ctl.formula.proposition;

import cz.muni.fi.ctl.formula.Formula;

/**
 * Created by daemontus on 23/09/14.
 */
public interface Proposition<T> extends Formula {

    public boolean evaluate(T value);

}
