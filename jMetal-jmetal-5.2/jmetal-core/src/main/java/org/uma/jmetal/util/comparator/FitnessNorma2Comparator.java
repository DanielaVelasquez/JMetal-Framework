package org.uma.jmetal.util.comparator;

import java.io.Serializable;
import java.util.Comparator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.impl.Fitness;

/**
 *
 * @author Daniel Pusil<danielpusil@uncauca.edu.co>
 */
public class FitnessNorma2Comparator<S extends Solution<?>> implements Comparator<S>, Serializable {

    private Fitness<S> solutionFitness = new Fitness<S>();

    /**
     * Compares two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
     * respectively.
     */
    @Override
    public int compare(S solution1, S solution2) {
        if (solution1 == null) {
            return 1;
        } else if (solution2 == null) {
            return -1;
        }

        double fitness1 = solution1.getObjective(0);
        double fitness2 = solution2.getObjective(0);
        if (fitness1 < fitness2) {
            return -1;
        }

        if (fitness1 > fitness2) {
            return 1;
        }
        if (solution1.getAttribute("B") != null && solution2.getAttribute("B") != null) {
            return compareNorma(solution1, solution2);
        }

        return 0;
    }

    private int compareNorma(S solution1, S solution2) {
        double BSolution1 = (double) solution1.getAttribute("B");
        double BSolution2 = (double) solution2.getAttribute("B");
        if (BSolution1 < BSolution2) {
            return -1;
        }

        if (BSolution1 > BSolution2) {
            return 1;
        }
        return 0;
    }
}
