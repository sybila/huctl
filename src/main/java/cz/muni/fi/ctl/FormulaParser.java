package cz.muni.fi.ctl;

import cz.muni.fi.ctl.antlr.CTLLexer;
import cz.muni.fi.ctl.antlr.CTLParser;
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class FormulaParser {

    @NotNull
    public Formula parse(@NotNull File input) throws IOException {
        @Nullable InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(input);
            return parse(new ANTLRInputStream(inputStream));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @NotNull
    public Formula parse(@NotNull String input) throws IllegalStateException {
        return parse(new ANTLRInputStream(input.toCharArray(), input.length()));
    }

    private Formula parse(ANTLRInputStream is) {
        CTLParser.RootContext root = new CTLParser(new BufferedTokenStream(new CTLLexer(is))).root();
        Collection<Proposition> propositions = root.propositions.values();
        Formula formula = root.result;
        findUnusedPropositions(propositions, formula);
        if (!propositions.isEmpty()) {
            throw new IllegalArgumentException("Unused proposition: "+propositions.iterator().next().toString());
        }
        return formula;
    }

    private void findUnusedPropositions(Collection<Proposition> propositions, Formula formula) {
        if (formula instanceof Proposition) {
            propositions.remove(formula);
            return;
        }
        for (Formula sub : formula.getSubFormulas()) {
            findUnusedPropositions(propositions, sub);
        }
    }

}
