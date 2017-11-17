package org.uma.jmetal.algorithm.singleobjective.LocalOptimizer;

import java.util.Comparator;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.solution.Solution;

public class OptSimulatedAnnealing<S extends Solution<?>> implements LocalSearchOperator<S> {

    /**
     * Atributes
     */
    private final Problem<S> problem;
    private final Comparator<S> comparator;
    private int evaluations;
    private int numberOfImprovements;

    private JMetalRandom randomGenerator;
    private int numberOfNonComparableSolutions;

    /**
     * Constructor. Creates a new local search object, A variant of Simulated
     * annealing, this change only a random variable in the process of mutation
     * (Tweak).
     *
     * @param improvementRounds number of iterations
     * @param mutationOperator mutation operator
     * @param comparator comparator to determine which solution is the best
     * @param problem problem to resolve
     *
     */
    public OptSimulatedAnnealing(int improvementRounds, MutationOperator<S> mutationOperator,
            Comparator<S> comparator, Problem<S> problem) {
        this.problem = problem;
        this.comparator = comparator;
        randomGenerator = JMetalRandom.getInstance();
        numberOfImprovements = 0;
        if (randomGenerator == null) {
            randomGenerator = JMetalRandom.getInstance();
        }
    }

    /**
     * Executes the local search.
     *
     * @param solution The solution to improve
     * @return An improved solution
     */
    @Override
    public S execute(S solution) {

        S originalSolution = (S) solution.copy();

        /**
         * Reset counters
         */
        evaluations = 0;
        numberOfNonComparableSolutions = 0;
        numberOfImprovements = 0;

        int best;

        /**
         * MA-ELM Parameters
         */
        double q = 0.9;     //% 冷却系数 Cooling factor
        double T0 = 20;    //% 初始温度   The initial temperature
        int Tend = 10; // % 终止温度   Termination temperature
        int L = 10; //%local search iteration  ->ITERACIONES

        /**
         * With this configuration of parameters the optimization is executed 70
         * times for each solution
         */
        while (T0 > Tend) {

            for (int i = 0; i < L; i++) {
                /**
                 * Esta implemetacion no nesecita mutationOperator
                 */
                S copySolution = (S) solution.copy();
                S mutatedSolution = tweak(copySolution);//MutationOperator
                problem.evaluate(mutatedSolution);
                evaluations++;
                /**
                 * Maximizer ..............................................
                 * Return 1 if mutedSolution is better than Solution*********
                 * Return 0 if both have the same Fittnes *******************
                 * Return -1 if Solution is better mutedSolution
                 *
                 */
                best = comparator.compare(mutatedSolution, copySolution);

                double decreTemp = solution.getObjective(0) - mutatedSolution.getObjective(0);
                double res_exp = Math.exp((-decreTemp) / T0);//Original version (MA-ELM)

                if (best == -1 || randomGenerator.nextDouble() <= res_exp) {//??siempre es verdadero
                    solution = mutatedSolution;
                    numberOfImprovements++;
                } else if (best == 0) {
                    numberOfNonComparableSolutions++;
                }
            }
            T0 = q * T0;
        }
        if (comparator.compare(solution, originalSolution) > 0) {
            solution = originalSolution;
        }
        return (S) solution.copy();
    }

    /**
     * Returns the number of evaluations (By default 70)
     *
     * @return Current evaluation
     */
    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public int getNumberOfImprovements() {
        return numberOfImprovements;
    }

    @Override
    public int getNumberOfNonComparableSolutions() {
        return numberOfNonComparableSolutions;
    }

    /**
     *
     * This variation of Simulated Annealing modifies a single random variable
     * of the solution, with radio=0.1 by default
     *
     * @param S Solution to mutate
     * @return S solution mutate
     */
    public final S tweak(Solution S) {
        Solution aux = S.copy();
        double radio = 0.1;

        if (randomGenerator == null) {
            randomGenerator = JMetalRandom.getInstance();
        }
        int randDimention = randomGenerator.nextInt(0, S.getNumberOfVariables() - 1);//Una sola dimension aleatoria según MA-ELM

        double res = ((double) S.getVariableValue(randDimention) - radio)
                + (2 * (radio * randomGenerator.nextDouble(-1.0, 1.0)));
        //Bounds control
        if ((res < -1)) {
            res = -1;
        }

        if ((res > 1)) {
            res = 1;
        }
        aux.setVariableValue(randDimention, res);

        return (S) aux;
    }

}
