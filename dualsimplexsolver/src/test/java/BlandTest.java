import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.naddeil.ro.api.Pair;
import it.naddeil.ro.dualsimplexsolver.Bland;
import it.naddeil.ro.dualsimplexsolver.Tableau;

public class BlandTest {

    @Test
    public void simpleBlandTest() {
        // Matrice:
        // 0 -1 1 0 0 0
        // 3 1 1 1 0 0
        // 1 1 2 0 1 0
        // 1 -1 2 0 0 1

        // La riga 2 minimzza il rapporto

        double[][] matrix = new double[][] { { 0, -1, 1, 0, 0, 0 }, { 3, 1, 1, 1, 0, 0 }, { 1, 1, 2, 0, 1, 0 }, { 1, 1, 2, 0, 0, 1 } };

        Tableau t = new Tableau(matrix);

        Pair newBase = Bland.getNewBase(t);

        assertEquals(1, newBase.getI());
        assertEquals(4, newBase.getJ());
    }

    @Test
    public void doppiaCoppiaBlandTest() {
        // Matrice:
        // 0 -1 1 0 0 0
        // 1 1 1 1 0 0
        // 1 1 2 0 1 0
        // 1 -1 2 0 0 1

        double[][] matrix = new double[][] { { 0, -1, 1, 0, 0, 0 }, { 1, 1, 1, 1, 0, 0 }, { 1, 1, 2, 0, 1, 0 }, { 1, -1, 2, 0, 0, 1 } };
        // La riga 2 minimzza il rapporto


        Tableau t = new Tableau(matrix);

        Pair newBase = Bland.getNewBase(t);

        assertEquals(1, newBase.getI());
        assertEquals(3, newBase.getJ());
    }
}
