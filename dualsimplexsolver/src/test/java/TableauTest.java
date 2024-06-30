import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.naddeil.ro.dualsimplexsolver.Tableau;

public class TableauTest {
    @Test
    public void testCreazione() {
        // Dato un problema del tipo
        // min z = 2x1 + 3x2
        // Soggetto ai vincoli
        // x1 + x2 <= 4
        // x1 + 2x2 <= 5
        // creiamo il tableau

        double[][] matrix = new double[][] { { 0, 2, 3, 0, 0 }, { 4, 1, 1, 1, 0 }, { 4, 1, 2, 0, 1 } };

        int[] base = new int[] { 3, 4 };
        int[] nonBase = new int[] { 1, 2 };
        int z = 0;
        double[] objectiveFunction = new double[] { 2, 3, 0, 0 };
        Tableau t = new Tableau(matrix);

        assert t.getMatrix() == matrix;
        assertArrayEquals(base, t.getBase());
        assertArrayEquals(nonBase, t.getNonBase());

        assertArrayEquals(objectiveFunction, t.getObjectiveFunction(), 0.0001);
        assertEquals(z, t.getZ(), 0.0001);

    }

    @Test
    public void testAzzera() {
        double[][] matrix = new double[][] { { 0, 2, 3, 0, 0 }, { 4, 1, 1, 1, 0 }, { 4, 1, 2, 0, 1 } };

        Tableau t = new Tableau(matrix);
        t.setMatrix(new double[][] { { 0, 2, 3, 2, 0 }, { 4, 1, 1, 1, 0 }, { 4, 1, 2, 0.5, 1 } });

        // Matrice:
        // 0 2 3 2 0
        // 4 1 1 1 0
        // 4 1 2 0.5 1
        // Azzeriamo colonna 3 riga 1
        t.azzera(3, 1);
        // Riga attesa 0: -8, 0, 1, 0, 0
        assertArrayEquals(new double[] { -8, 0, 1, 0, 0 }, t.getMatrix()[0], 0.0001);
        // Riga attesa 1: 2, .05, 1.5, 0, 1
        assertArrayEquals(new double[] { 2, 0.5, 1.5, 0, 1 }, t.getMatrix()[2], 0.0001);
    }

    

    @Test
    public void testPortaAUno() {
        double[][] matrix = new double[][] { { 0, 2, 3, 0, 0 }, { 4, 1, 1, 1, 0 }, { 4, 1, 2, 0, 1 } };

        Tableau t = new Tableau(matrix);
        t.setMatrix(new double[][] { { 0, 2, 3, 0, 0 }, { 4, 1, 1, 4, 0 }, { 4, 1, 2, 0, 1 } });

        // Matrice:
        // 0 2 3 2 0
        // 4 1 1 1 0
        // 4 1 2 0.5 1
        // Azzeriamo colonna 3 riga 1
        t.portaAUno(3, 1);
        // Riga attesa 1: 1, 0.25, 0.25, 1, 0
        assertArrayEquals(new double[] { 1, 0.25, 0.25, 1, 0 }, t.getMatrix()[1], 0.0001);
    }

}
