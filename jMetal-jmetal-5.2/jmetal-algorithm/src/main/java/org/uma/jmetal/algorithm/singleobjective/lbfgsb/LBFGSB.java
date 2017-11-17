package org.uma.jmetal.algorithm.singleobjective.lbfgsb;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.LowerTriangDenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.UpperTriangDenseMatrix;
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
     * Movement on individual [n, m]
     */
    private List<DenseVector> S;
    /**
     * Movement on individual's gradient [n, m]
     */
    private List<DenseVector> Y;
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
    /**
     * Quasi Newton iterations
     */
    private int k;
    
    private double theta;

    
    /**-----------------------------------------------------------------------------------------
     * Methods
     *-----------------------------------------------------------------------------------------*/
    
    
    
    
    public LBFGSB(DoubleProblem problem, Comparator<DoubleSolution> comparator, int FE, double penalize_value, int m, double eps)
    {
        this.problem = problem;
        this.comparator = comparator;
        this.FE = FE;
        this.penalize_value = penalize_value;
        this.m = m;
        this.eps = eps;
        theta = 1;
    }


    @Override
    public void run() {
        k = 0;

        this.offspring_population = new ArrayList<>();
        
        //Create initial solution if necessary
        this.n = this.problem.getNumberOfVariables();
        if(this.x == null)
        {
            this.x = problem.createSolution();
            this.evaluate(x);
        }
                //Obtain gradient
        try
        {
            this.gradient = (DenseMatrix) this.x.getAttribute("g");
        }catch(Exception e)
        {
            throw new JMetalException("Solution doesn't have a gradient assign on its attributes");
        }
        
        this.S = new ArrayList<>();
        this.Y = new ArrayList<>();
        this.W = new DenseMatrix(n, 1);
        this.M = new DenseMatrix(1, 1);

        while(!isStoppingConditionReached())
        {
            double f_old = this.x.getObjective(0);
            DenseMatrix x_old = this.getMatrixFrom(this.x);
            DenseMatrix g_old = this.gradient.copy();
            
            this.getGeneralizedCauchyPoint(this.x, x_old, gradient);
            this.subspaceMin(x, x_old, Matrices.getColumn(gradient, 0));
            
            double alpha = 1;
            
            if(line_searchflag)
            {
                DenseMatrix p = new DenseMatrix(xBar);
                DenseMatrix x_1 = new DenseMatrix(x_old);
                x_1.scale(-1);
                p.add(x_1);                
                alpha = strongWolfe(x, gradient, p);
            }
            
            for (int j = 0; j < n; j++)
            {
                x.setVariableValue(j, x.getVariableValue(j) + alpha * (xBar.get(j, 0) - x.getVariableValue(j)));
            }
            
            this.evaluate(x);
            
            DenseMatrix xMatrix = this.getMatrixFrom(x);
            
            //Obtain gradient
            try
            {
                this.gradient = (DenseMatrix) this.x.getAttribute("g");
            }
            catch(Exception e)
            {
                throw new JMetalException("Solution doesn't have a gradient assign on its attributes");
            }
            
            //y = grandient - g_old
            g_old.scale(-1);
            DenseMatrix yMatrix = new DenseMatrix(gradient);
            yMatrix.add(g_old);
            DenseVector y = Matrices.getColumn(yMatrix, 0);
            
            //s = xMatrix - x_old
            x_old.scale(-1);
            DenseMatrix sMatrix = new DenseMatrix(xMatrix);
            sMatrix.add(x_old);
            DenseVector s = Matrices.getColumn(sMatrix, 0);
            
            double curv = Math.abs(s.dot(y));
            
            if(curv < eps)
            {
                k++;
                continue;
            }
            
            if (k < m)
            {
                Y.add(y);
                S.add(s);
            }
            else
            {
                Y.remove(0);
                Y.add(y);
                S.remove(0);
                S.add(s);
            }
            
            theta = (double) y.dot(y) / (double) y.dot(s);
            yMatrix = this.getMatrixFrom(Y);
            sMatrix = this.getMatrixFrom(S);
            
            DenseMatrix sMatrixTheta = new DenseMatrix(sMatrix);
            sMatrixTheta.scale(theta);
            
            W = new DenseMatrix(n, 2 * ((k > m)? m : k + 1));
            this.mergeMatrixesColumns(yMatrix, sMatrixTheta, W);
            
            DenseMatrix transposeSMatrix = new DenseMatrix(sMatrix.numColumns(), sMatrix.numRows());
            sMatrix.transpose(transposeSMatrix);
            
            //A = transpose(sMatrix) * yMatrix
            DenseMatrix A = new DenseMatrix(transposeSMatrix.numRows(), yMatrix.numColumns());
            transposeSMatrix.mult(yMatrix, A);
            
            DenseMatrix L = this.trill(A);
            
            DenseMatrix D = new DenseMatrix(new LowerTriangDenseMatrix(new UpperTriangDenseMatrix(A.copy())));
            D.scale(-1);
            
            DenseMatrix transposeL = new DenseMatrix(L);
            transposeL.transpose();
            
            DenseMatrix up = new DenseMatrix(D.numRows(), D.numColumns() + L.numColumns());
            this.mergeMatrixesColumns(D, transposeL, up);
            
            DenseMatrix transposeS = new DenseMatrix(sMatrix.numColumns(),sMatrix.numRows());
            sMatrix.transpose(transposeS);
            
            //transposeSS = theta * transpose(sMatrix) * sMatrix
            DenseMatrix transposeSS = new DenseMatrix(transposeS.numRows(), sMatrix.numColumns());
            transposeS.mult(sMatrix, transposeSS);
            transposeSS.scale(theta);            
            
            DenseMatrix down = new DenseMatrix(L.numRows(), L.numColumns() + transposeSS.numColumns());
            this.mergeMatrixesColumns(L, transposeSS, down);            
            
            DenseMatrix MM = new DenseMatrix(2 * ((k > m)? m : k + 1), 2 * ((k > m)? m : k + 1));
            this.mergeMatrixesRows(up, down, MM);
            
            M = new DenseMatrix(MM.numRows(), MM.numColumns());
            DenseMatrix I = Matrices.identity(MM.numColumns());
            MM.solve(I, M);
            
            k++;
        }
    }

    @Override
    public DoubleSolution getResult() {
        return x;
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
        
        
        // p = transpose(W) * d
        DenseVector p = new DenseVector(W.numColumns());
        WTranspose.mult(d, p);
 
        //c = 0
        c = new DenseMatrix(W.numColumns(), 1);
        this.fillMatrixWith(c, 0);
        
        //f' = gT = -transpose(d) * d
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
            c.add(matrix_p.scale(deltha_t));
            
            //b-th row of the marix W
            DenseVector wbt = this.getRow(W, b);//Matrices.getColumn(W, b);
            
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
            DenseVector mwb = this.getRow(W, b);//Matrices.getColumn(W, b);
            
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
            DenseVector wb = this.getRow(W, b);//Matrices.getColumn(W, b);
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
                m.set(i, j, value);
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
        DenseVector wtzr = new DenseVector(wtz.numRows());
        wtz.mult(r, wtzr);
        
        //v = M * (wtz * r) 
        DenseVector v = new DenseVector(wtzr.size());
        M.mult(wtzr, v);
        
        DenseMatrix wtzTranspose = new DenseMatrix(wtz.numColumns(), wtz.numRows());
        wtz.transpose(wtzTranspose);
        
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
        DenseMatrix transposeWtz  = new DenseMatrix(wtz.numColumns(), wtz.numRows());
        wtz.transpose(transposeWtz);
        
        //wtz_t_v = transpose(wtz) * v
        DenseVector wtz_t_v = new DenseVector(transposeWtz.numRows());
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
            xBar.set(idx, 0, (xBar.get(idx, 0) + dStart.get(k)));
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
    
    private double strongWolfe(DoubleSolution x0, DenseMatrix g, DenseMatrix p)
    {
        double c_1 = 1.e-4;
        double c_2 = 0.9;
        double alphaMax = 2.5;
        double alphaIml = 0;
        double alphaI = 1;
        double fIml = x0.getObjective(0);
        double f0 = x0.getObjective(0);
        
        //transposeG = transpose(g)
        DenseMatrix transposeG = new DenseMatrix(g.numColumns(), g.numRows());
        g.transpose(transposeG);
        
        //dphi0 = transpose(g) * p
        DenseMatrix dphi0Matrix = new DenseMatrix(transposeG.numRows(), p.numColumns());
        transposeG.mult(p, dphi0Matrix);
        double dphi0 = dphi0Matrix.get(0, 0);
        
        int i = 0;
        int maxIters = 20;
        
        DenseMatrix alphaIp = new DenseMatrix(p);
        alphaIp.scale(alphaI);
        double alpha = -1;
                
        while(true)
        {
            DoubleSolution x = (DoubleSolution) x0.copy();
            
            for(int j = 0; j < n; j++)
            {
                x.setVariableValue(i, x.getVariableValue(i) + alphaIp.get(j, 0));                
            }
            
            this.evaluate(x);
            
            DenseMatrix gi;
            
            try
            {
                gi = (DenseMatrix) x.getAttribute("g");
            }
            catch(Exception e)
            {
                throw new JMetalException("Solution doesn't have a gradient assign on its attributes");
            }
            
            double fi = x.getObjective(0);
            
            
            if((fi > (f0 + c_1 * dphi0)) || (i > 1) && (fi >= fIml))
            {
                alpha = alphaSum(x0, f0, g, p, alphaIml, alphaI);
                break;
            }
            DenseMatrix giCopy = new DenseMatrix(gi);
            gi = new DenseMatrix(giCopy.numColumns(), giCopy.numRows());
            giCopy.transpose(gi);
            
            DenseMatrix gip = new DenseMatrix(gi.numRows(), p.numColumns());
            gi.mult(p, gip);
            double dphi = gip.get(0, 0);
            
            if(Math.abs(dphi) <= (-c_2 * dphi0))
            {
                alpha = alphaI;
            }
            
            if(dphi >= 0)
            {
                alpha = alphaSum(x0, f0, g, p, alphaI, alphaIml);
                break;
            }
            
            alphaIml = alphaI;
            fIml = fi;
            alphaI = alphaI + 0.8 * (alphaMax - alphaI);
            
            if(i > maxIters)
            {
                alpha = alphaI;
                break;
            }

            i++;
        }
        
        return alpha;
    }
    
    
    private double alphaSum(DoubleSolution s, double f0, DenseMatrix g0, DenseMatrix p, double alphaLo, double alphaHigh)
    {
        double c_1 = 1.e-4;
        double c_2 = 0.9;
        int i = 0;
        int maxIters = 20;
        
        //transposeG0 = transpose(g0)
        DenseMatrix transposeG0 = new DenseMatrix(g0.numColumns(), g0.numRows());
        g0.transpose(transposeG0);
        
        //dphi0 = transpose(g0) * p
        DenseMatrix dphi0Matrix = new DenseMatrix(transposeG0.numRows(), p.numColumns());
        transposeG0.mult(p, dphi0Matrix);
        double dphi0 = dphi0Matrix.get(0, 0);        
        double alpha = -1;
        
        while(true)
        {
            double alphaI = 0.5 * (alphaLo + alphaHigh);
            alpha = alphaI;
            
            DenseMatrix alphaIp = new DenseMatrix(p);
            alphaIp.scale(alphaI);
            
            DoubleSolution x = (DoubleSolution) s.copy();
            
            for(int j = 0; j < n; j++)
            {
                x.setVariableValue(i, x.getVariableValue(i) + alphaIp.get(j, 0));                
            }
            
            this.evaluate(x);
            
            DenseMatrix gi;
            
            try
            {
                gi = (DenseMatrix) x.getAttribute("g");
            } 
            catch(Exception e)
            {
                throw new JMetalException("Solution doesn't have a gradient assign on its attributes");
            }
            
            double fi = x.getObjective(0);
            
            DenseMatrix alphaLoP = new DenseMatrix(p);
            alphaLoP.scale(alphaLo);
            
            DoubleSolution xLo = (DoubleSolution) s.copy();
            
            for(int j = 0; j < n; j++)
            {
                xLo.setVariableValue(i, xLo.getVariableValue(i) + alphaLoP.get(j, 0));
            }
            
            this.evaluate(xLo);            
            double fLo = xLo.getObjective(0);
            
            if(fi > (f0 + c_1 * alphaI * dphi0) || (fi >= fLo))
            {
                alphaHigh = alphaI;
            }
            else
            {
                gi.transpose();
                DenseMatrix gis = new DenseMatrix(gi.numRows(), p.numColumns());
                gi.mult(p, gis);
                
                double dphi = gis.get(0, 0);
                
                if (Math.abs(dphi) <= (-c_2 * dphi0))
                {
                    alpha = alphaI;
                    break;
                }
                
                if (dphi * (alphaHigh - alphaLo) >= 0)
                {
                    alphaHigh = alphaLo;
                }
                
                alphaLo = alphaI;
            }
            
            i++;
            
            if (i > maxIters)
            {
                alpha = alphaI;
                break;
            }
        }
        
        return alpha;
    }
    
    /**
     * Create a matrix from a column vectors list
     * @param columnVectors list of column vectors
     * @return matrix with columns from the list
     */
    private DenseMatrix getMatrixFrom(List<DenseVector> columnVectors)
    {
        int columns = columnVectors.size();        
        DenseVector first = columnVectors.get(0);
        int rows = first.size();
        DenseMatrix m = new DenseMatrix(rows, columns);
        
        
        for(int j = 0; j < columns; j++)
        {
            DenseVector v = columnVectors.get(j);
            
            for(int i = 0; i < rows; i++)
            {
                m.set(i, j, v.get(i));
            }
        }
        
        return m;                
    }
    
    /**
     * Put in matrix to = [A B], merge two matrixes in one
     * @param A first matrix
     * @param B second matrix
     * @param to matrix where A and B are going to be placed
     */
    private void mergeMatrixesColumns(DenseMatrix A, DenseMatrix B, DenseMatrix to)
    {
        int rowsA = A.numRows();
        int rowsB = B.numRows();
        int rowsTo = to.numRows();
        int colsA = A.numColumns();
        int colsB = B.numColumns();
        int colsTo = to.numColumns();
        int sumColsAColsB = colsA + colsB;
        
        if ((rowsA != rowsB) ||  (rowsA != rowsTo))          
        {
            throw new JMetalException("Number of rows must be equals between all the matrixes");
        }
        
        if ((colsA + colsB) > colsTo)
        {
            throw new JMetalException("Number of columns of matrix A and B can't be greater than the columns of to matrix");
        }
        
        for (int i = 0; i < rowsA; i++)
        {
            for (int j = 0; j < colsA; j++)
            {
                to.set(i, j, A.get(i, j));                
            }
            
            for (int j = colsA; j < sumColsAColsB; j++)
            {
                to.set(i, j, B.get(i, j - colsA));
            }
        }
    }    
    
    /**
     * Put in matrix to = [A;B], merge two matrixes in one
     * @param A first matrix
     * @param B second matrix
     * @param to matrix where A and B are going to be placed
     */
    private void mergeMatrixesRows(DenseMatrix A, DenseMatrix B, DenseMatrix to)
    {
        int rowsA = A.numRows();
        int rowsB = B.numRows();
        int rowsTo = to.numRows();
        int colsA = A.numColumns();
        int colsB = B.numColumns();
        int colsTo = to.numColumns();
        int sumRowsARowsB = rowsA + rowsB;
        
        if ((colsA != colsB) || (colsA != colsTo))          
        {
            throw new JMetalException("Number of columns must be equals between all the matrixes");
        }
        
        if ((rowsA + rowsB) > rowsTo)
        {
            throw new JMetalException("Number of rows of matrix A and B can't be greater than the rows of to matrix");
        }
        
        for (int j = 0; j < colsA; j++)
        {
            for (int i = 0; i < rowsA; i++)
            {
                to.set(i, j, A.get(i, j));                
            }
            
            for (int i = rowsA; i < sumRowsARowsB; i++)
            {
                to.set(i, j, B.get(i - rowsA, j ));
            }
        }
    }
    
    /**
     * return the elements on and below the -1 diagonal of d
     * @param d matrix to get the diagonal
     * @return a matrix with elements below the -1 diagonal
     */
    private DenseMatrix trill(DenseMatrix d)
    {
        LowerTriangDenseMatrix L = new LowerTriangDenseMatrix(d);
        
        for(int i = 0; i < d.numColumns(); i++)
        {
            L.set(i, i, 0);
        }
        
        return new DenseMatrix(L);
    }

    public void setX(DoubleSolution x) {
        this.x = x;
    }
    
    public DenseVector getRow(DenseMatrix A, int row)
    {
        int size = A.numColumns();
        DenseVector v = new DenseVector(size);
        
        for(int i = 0; i < size; i++)
        {
            v.add(i, A.get(row, i));
        }
        return v;
    }

            
}
