package it.naddeil.ro.dualsimplexsolver;

import it.naddeil.ro.common.models.Result;
import it.naddeil.ro.common.utils.Value;

public class DualSimplexSolver {

    public Result riottimizza(Value[][] tableau) {
        SimplessoDualeFrazionario dualSimplex = new SimplessoDualeFrazionario(tableau);
        dualSimplex.solve();
        return Result.fromTableau(dualSimplex.getTableau()).setOut(dualSimplex.getPassaggi());
    }

}
