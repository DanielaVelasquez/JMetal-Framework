package org.uma.jmetal.algorithm.singleobjective.lbfgsb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrices;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

public class LBFGSB implements Algorithm<DoubleSolution>
{

    /**-----------------------------------------------------------------------------------------
     * Atributes
     *-----------------------------------------------------------------------------------------*/
    
    /**
     * Problem to solve
     */
    private DoubleProblem problem ;
    /**
     * Determines how a solution should be order
     */
    private Comparator<DoubleSolution> comparator;
    /**
     * Solution to evolve on 
     */
    private DoubleSolution x;
    /**
     * Solution` gradient [n, 1]
     */
    private DenseMatrix gradient;
    /**
     * Maximun number of function evaluations
     */
    private int FE;
    /**
     * Number of evaluations made
     */
    private int evaluations;
     /**
     * Individiuals produced on algorithm
     */
    private List<DoubleSolution> offspring_population;
    /**
     * Value to set to solutions when evaluations are over
     */
    private double penalize_value;
    /**
     * Number of corrections 
     */
    private int m;
    /**
     * Number of solution variables
     */
    private int n;
    /**
     * Movement on individual
     */
    private DenseMatrix S;
    /**
     * Movement on individual's gradient
     */
    private DenseMatrix Y;
    /**
     * Generalized Cauchy point [n, 1]
     */
    private DenseMatrix xCauchy;
    /**
     * Initialization vector for subspace minimization [2m, 1]
     */
    private DenseMatrix c;
    /**
     * BFGS matrix storage [n, 2m]
     */
    private DenseMatrix W;
    /**
     * BFGS-B matrix storage [2m, 2m]
     */
    private DenseMatrix M;
    /**
     * 
     */
    private double eps;
    /**
     * 
     */
    private boolean line_searchflag;
    /**
     * [n, 1]
     */
    private DenseMatrix xBar;
    
    private double theta;

    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    
    
    public LBFGSB()
    {
        theta = 1;
    }

    @Override
    public void run() {
        //Obtain gradient
        try
        {
            this.gradient = (DenseMatrix) this.x.getAttribute("g");
        }catch(Exception e)
        {
            throw new JMetalException("Solution doesn't have a gradient assign on its attributes");
        }
        
        //Create initial solution if necessary
        this.n = this.problem.getNumberOfVariables();
        if(this.x == null)
        {
            this.x = problem.createSolution();
            this.evaluate(x);
        }
        
        this.S = new DenseMatrix(n,m);
        this.Y = new DenseMatrix(n,m);
        this.W = new DenseMatrix(n, 2 * m);
        this.M = new DenseMatrix(2 * m, 2 * m);

        while(!isStoppingConditionReached())
        {
            double f_old = this.x.getObjective(0);
            DenseMatrix x_old = this.getMatrixFrom(this.x);
            DenseMatrix g_old = this.gradient.copy();
            
            this.xCauchy = new DenseMatrix(this.n, 1);
            this.c = new DenseMatrix(this.m * 2, 1);
            
            this.getGeneralizedCauchyPoint(this.x, x_old, gradient);
            
            
        }
    }

    @Override
    public DoubleSolution getResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "L-BFGS-B";
    }

    @Override
    public String getDescription() {
        return "L-BFGS-B is a limited memory algorithm for solving large nonlinear "+
               "optimization problems subject to simple bounds on the variables";
    }
    
        /**
     * Evaluates an individual
     * @param solution 
     */
    protected void evaluate(DoubleSolution solution)
    {
        if(!isStoppingConditionReached())
        {
            this.problem.evaluate(solution);
            this.offspring_population.add((DoubleSolution)solution.copy());
            this.updateProgress();
        }
        else
        {
            this.penalize(solution);
        }
    }
    /**
     * Increments the number of generations evaluated
     */
    protected void updateProgress() {
        evaluations += 1;
    }
    /**
     * Determines if the stopping was reached
     * @return true if stopping condition was reached, false otherwise
     */
        private boolean isStoppingConditionReached() {
        return evaluations >= FE;
    }
    /**
     * Gets the best individual between two individuals, if they are equals
     * the firts indiviudal is return by default
     * @param s1 first individual
     * @param s2 seconde individual
     * @return best individual between s1 and s2
     */
    protected DoubleSolution getBest(DoubleSolution s1, DoubleSolution s2)
    {
        int comparison = comparator.compare(s1, s2);
        if(comparison == 0)
        {
            try
            {
                double b_s1 = (double) s1.getAttribute("B");
                double b_s2 = (double) s2.getAttribute("B");
                if(b_s1 <= b_s2)
                    return s1;
                else
                    return s2;
            }
            catch(Exception e)
            {
                return s1;
            }
        }
        else if(comparison < 1)
            return s1;
        else
            return s2;
    }
    /**
     * Sets an individual function value to a penalization value given by 
     * the user
     * @param solution solution to penalize with a bad function value
     */
    protected void penalize(DoubleSolution solution){
        solution.setObjective(0, this.penalize_value);
    }
    /**
     * Creates a nx1 matrix from a solution's variables
     * @param s solution to represent with a Matrix
     * @return an nx1 matrix with s's variables
     */
    private DenseMatrix getMatrixFrom(DoubleSolution s)
    {
        DenseMatrix m = new DenseMatrix(this.n,1);
        for(int i = 0; i < this.n; i++)
        {
            m.set(i, 0, s.getVariableValue(i));
        }
        return m;
    }
    private void getGeneralizedCauchyPoint(DoubleSolution s, DenseMatrix x, DenseMatrix g)
    {
        HashMap<Integer, Double> T = new HashMap<Integer, Double>();
        DenseVector d = new DenseVector(this.n);
        List<Integer> indexes = new ArrayList<>();
        //Define breakpoints in each coordinate direction. Compute t
        for(int i = 0; i < n; i++)
        {
            indexes.add(i);
            double gradient = g.get(i, 0);
            double t_i = 0;
            if( gradient == 0)
                t_i = Double.POSITIVE_INFINITY;
            else if(gradient < 0)
                t_i = (s.getVariableValue(i) - s.getUpperBound(i)) / gradient;
            else
                t_i = (s.getVariableValue(i) - s.getLowerBound(i)) / gradient;
           T.put(i, t_i);
           d.set(i, -g.get(i, 0));
           
        }
        
        //Sort indexes to obtain ordered sets t_i
        List sorted_indexes = T.entrySet().stream()
                                     .sorted(Map.Entry.comparingByValue())
                                     .map(e -> new Integer(e.getKey()))
                                     .collect(Collectors.toList());
        this.xCauchy = x;
        
        DenseMatrix WTranspose = new DenseMatrix(W.numColumns(), W.numRows());
        W.transpose(WTranspose);
        
        
        // p = W^T * d
        DenseVector p = new DenseVector(W.numColumns());
        WTranspose.mult(d, p);
 
        //c = 0
        c = new DenseMatrix(2 * m, 1);
        this.fillMatrixWith(c, 0);
        
        //f' = gT = -dTd
        DenseVector d_1 = new DenseVector(d);
        d_1.scale(-1);
        double f_prime = d.dot(d_1);
        
        double fpp0 = -theta * f_prime;
        
        //f'' = -theta*f' - pT*M*p
        DenseVector Mp = new DenseVector(p.size());
        M.mult(p, Mp);
        double f_doubleprime = fpp0 - p.dot(Mp);
        
        double deltha_t_min = -f_prime/f_doubleprime;
        
        double t_old = 0;
        // b := 	argmin {t_i , t_i >0}
        int i = 0;
        for (int j = 0; j < n; j++) {
                i = j;
                if (T.get(sorted_indexes.get(j)) > 0)
                        break;
        }
        int b = (int) sorted_indexes.get(i);
        double t = T.get(b);
        double deltha_t = t;
        
        while((deltha_t_min >= deltha_t) && (i < n))
        {
            double gradient = g.get(b, 0);
            
            double db = d.get(b);
            if(db > 0)
                xCauchy.set(b, 0, s.getUpperBound(b));
            else if(db < 0 )
                xCauchy.set(b, 0, s.getLowerBound(b));
           
            // z_b = xCauchy[b] - x[b]
            double zb = xCauchy.get(b, 0) - s.getVariableValue(b);
            
            //c = c + deltha_t * p
            DenseMatrix matrix_p = this.makeMatrixFrom(p);
            c = (DenseMatrix) c.add(matrix_p.scale(deltha_t));
            
            //b-th row of the marix W
            DenseVector wbt = Matrices.getColumn(W, b);
            
            //b-th row of the marix -> W[b] * g^2
            DenseVector wbtG2 = wbt.copy();
            wbtG2.scale(gradient * gradient);
            
            //Mc = M * c
            DenseMatrix Mc = new DenseMatrix(M.numRows(), c.numColumns());
            M.mult(c, Mc);
            DenseVector McVector = Matrices.getColumn(Mc, 0);
            
            //MpVector = M * p
            DenseVector MpVector = new DenseVector(p.size());
            M.mult(p, MpVector);
            
            f_prime +=   deltha_t * f_doubleprime + 
                         (Math.pow(gradient, 2)) + 
                         theta * gradient * zb - 
                         wbt.scale(gradient).dot(McVector);        
            
            //Since this operation was performed wbt.scale(gradient) now 
            //wbt is b-th row of the marix -> W[b] * g
            
            //mwb = M * (W[b])^T
            DenseVector mwb = Matrices.getColumn(W, b);
            
            //This operation transpose mwb and matrixMwb contains in one column
            //the values of mwb
            DenseMatrix matrixMwb = this.makeMatrixFrom(mwb);
            M.mult(matrixMwb, matrixMwb);
            mwb = Matrices.getColumn(matrixMwb, 0);
            
            f_doubleprime += -theta * gradient * gradient -
                             2.0 * wbt.dot(MpVector) -
                             wbtG2.dot(mwb);
            
            f_doubleprime = Math.max(eps * fpp0, f_doubleprime);
            
            //p = p + g * W[b]
            DenseVector wb = Matrices.getColumn(W, b);
            p = (DenseVector) p.add(wb.scale(gradient));
            
            //d[b] = 0
            d.set(b, 0);
            
            //delta_t_min = - f'/f''
            deltha_t_min = - (f_prime / f_doubleprime);
           
            t_old = t;
            
            i++;
            if (i < n) 
            {
                b = (int) sorted_indexes.get(i);
                t = T.get(b);
                
                deltha_t = t - t_old;
            }
            
            deltha_t_min = Math.max(deltha_t_min, 0);
            
            t_old = t_old + deltha_t_min;
            
            for(int j = i; i < n; i++)
            {
                int idx = (int) sorted_indexes.get(j);
                xCauchy.set(idx, 0, s.getVariableValue(idx) + t_old * d.get(idx));
            }
            
            p.scale(deltha_t_min);
            DenseMatrix pMatrix = this.makeMatrixFrom(p);
            c.add(pMatrix);           
        }        
    }
    
    private void fillMatrixWith(DenseMatrix m, double value)
    {
        int rows = m.numRows();
        int cols = m.numColumns();
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                m.set(rows, cols, value);
            }
        }
    }
    /**
     * Creates matriz, from a vector where the vector is tha matrix first column
     * @param v vector to pass in a matrix
     * @return vector as a column in a matrix
     */
    private DenseMatrix makeMatrixFrom(DenseVector v)
    {
        int size = v.size();
        DenseMatrix m = new DenseMatrix(size, 1);
        for(int i = 0; i<size; i++){
            m.set(i, 0, v.get(i));
        }
        return m;
    }
    
    /**
     * Subspace minimization for the cuadratic over free variables
     * @param s is the solution
     * @param x is the solution represented in a matrix
     * @param g is the gradient at x point
     */
    private void subspaceMin(DoubleSolution s, DenseMatrix x, DenseVector g)
    {
        line_searchflag = true;
        
        List<Integer> freeVarsIdx = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            double value = xCauchy.get(i, 0);
            if ((value != s.getUpperBound(i)) && (value != s.getLowerBound(i)))
            {
                freeVarsIdx.add(i);
            }
        }
        
        int freeVarsSize = freeVarsIdx.size();
        
        if (freeVarsSize == 0)
        {
            xBar = xCauchy.copy();
            line_searchflag = false;
            return;
        }
        
        DenseMatrix Z = new DenseMatrix(n, freeVarsSize);
        
        for(int i = 0; i < freeVarsSize; i++)
        {
            Z.set(freeVarsIdx.get(i), i, 1);
        }
        
        DenseMatrix wTranspose = new DenseMatrix(W.numColumns(), W.numRows());
        W.transpose(wTranspose);
        
        //Compute W^T * Z the restriction of W to free variables
        DenseMatrix wtz = new DenseMatrix(wTranspose.numRows(), Z.numColumns());
        wTranspose.mult(Z, wtz);
        
        /**
         * In this section the variable rr is going to be computed
         */
        
        //x_minus = -x
        DenseMatrix x_minus = new DenseMatrix(x);
        x_minus.scale(-1);
        
        //xc_x = xCauchy - x
        DenseMatrix xc_x = new DenseMatrix(xCauchy);
        xc_x.add(x_minus);
        
        //xc_x = theta * (xCauchy - x)
        xc_x.scale(theta);
        
        //mc = M * c
        DenseMatrix mc = new DenseMatrix(M.numRows(), c.numColumns());
        M.mult(c, mc);
        
        //wmc = w * (M * c)
        DenseMatrix wmc = new DenseMatrix(W.numRows(), mc.numColumns());
        W.mult(mc, wmc);
        
        //wmc = - (w * (M * c))
        wmc.scale(-1);
        
        //xc_x = theta * (xCauchy - x) - (w * (M * c))
        xc_x.add(wmc);
        
        DenseVector rrOperator = Matrices.getColumn(xc_x, 0);
        
        //rr = g + theta * (xCauchy - x) - (w * (M * c))
        DenseVector rr = new DenseVector(g);
        rr.add(rrOperator);
        /**
         * End rr section
         */
        
        DenseVector r = new DenseVector(freeVarsSize);
        
        //Compute the reduce gradient of mk restricted to free variables
        for(int i = 0; i < freeVarsSize; i++)
        {
            r.set(i, rr.get(freeVarsIdx.get(i))); 
        }
        
        //Form intermediate variables
        double inv_theta = 1 / theta;
        
        /**
         * This section is going to compute variable v
         */        
        
        //wtzr = wtz * r
        DenseVector wtzr = new DenseVector(r.size());
        wtz.mult(r, wtzr);
        
        //v = M * (wtz * r) 
        DenseVector v = new DenseVector(wtzr.size());
        M.mult(wtzr, v);
        
        DenseMatrix wtzTranspose = new DenseMatrix(wtz);
        wtzTranspose.transpose();
        
        //invThethaWtz = int_theta * wtz
        DenseMatrix invThethaWtz = new DenseMatrix(wtz);
        invThethaWtz.scale(inv_theta);
        
        //n = inv_theta * wtz * transpose(wtz)
        DenseMatrix n = new DenseMatrix(invThethaWtz.numRows(), wtzTranspose.numColumns());
        invThethaWtz.mult(wtzTranspose, n);
        
        //mn = M * n
        DenseMatrix mn = new DenseMatrix(M.numRows(), n.numColumns());
        M.mult(n, mn);        
        mn.scale(-1);
        
        //n = identity(size(n)) - M * n
        DenseMatrix i = Matrices.identity(n.numColumns());
        i.add(mn);        
        n = i;
        
        DenseMatrix identity = Matrices.identity(n.numRows());
        
        //invN = inverse(n)
        DenseMatrix invN = new DenseMatrix(n.numRows(), n.numColumns());
        n.solve(identity, invN);
        
        //nv = inverse(n) * v
        DenseVector nv = new DenseVector(v.size());
        invN.mult(v, nv);
        
        //v = inverse(n) * v
        v = nv;
        
        //transposeWtz = transpose(wtz)
        DenseMatrix transposeWtz  = new DenseMatrix(wtz);
        transposeWtz.transpose();
        
        //wtz_t_v = transpose(wtz) * v
        DenseVector wtz_t_v = new DenseVector(v.size());
        transposeWtz.mult(v, wtz_t_v);
        
        //wtz_t_v = - (1/theta)^2 * transpose(wtz) * v
        wtz_t_v.scale(-(inv_theta * inv_theta));
        
        //invThetaR = - (1/theta) * r
        DenseVector invThetaR = new DenseVector(r);
        invThetaR.scale(-inv_theta);
        
        //du = - (1/theta) * r - (1/theta)^2 * transpose(wtz) * v
        DenseVector du = new DenseVector(invThetaR);
        du.add(wtz_t_v);
        
        double alphaStar = findAlpha(s, du, freeVarsIdx);
        DenseVector dStart = new DenseVector(du);
        dStart.scale(alphaStar);
        xBar = xCauchy.copy();
        
        for(int k = 0; k < freeVarsSize; k++)
        {
            int idx = freeVarsIdx.get(k);
            xBar.set(idx, 0, (xBar.get(idx, 0) + dStart.get(idx)));
        }        
    }
    
    
    /**
     * Calculate the positive scaling parameter
     * @param s is the current solution
     * @param du is the solution of unconstrained minimization
     * @param freeVarsIdx is free variables
     * @return the positive scaling parameter
     */
    private double findAlpha (DoubleSolution s, DenseVector du, List<Integer> freeVarsIdx)
    {
        double alphaStart = 1;
        int freeVarIdxSize = freeVarsIdx.size();
        
        for(int i = 0; i < freeVarIdxSize; i++)
        {
            int idx = freeVarsIdx.get(i);            
            double duValue = du.get(i);
            double cauchyValue = xCauchy.get(i, 0);
            
            if(duValue > 0)
            {
                alphaStart = Math.min(alphaStart, (s.getUpperBound(idx) - cauchyValue) / duValue);
            }
            else
            {
                alphaStart = Math.min(alphaStart, (s.getLowerBound(idx) - cauchyValue) / duValue);
            }
        }
        
        return alphaStart;
    }
            
}
