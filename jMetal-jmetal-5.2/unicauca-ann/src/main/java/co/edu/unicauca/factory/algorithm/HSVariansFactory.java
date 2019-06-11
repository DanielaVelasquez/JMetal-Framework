package co.edu.unicauca.factory.algorithm;

import org.uma.jmetal.algorithm.singleobjective.harmonysearch.GHSBuilder;
import org.uma.jmetal.algorithm.singleobjective.harmonysearch.HSBuilder;
import org.uma.jmetal.algorithm.singleobjective.harmonysearch.IHSBuilder;
import org.uma.jmetal.algorithm.singleobjective.harmonysearch.NGHSBuilder;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;

import co.edu.unicauca.factory.parameters.AbstractParametersFactory;
import co.edu.unicauca.problem.AbstractELMEvaluator;

/**
 *
 * @author Daniel Pusil <danielpusil@unicauca.edu.co>
 */
public class HSVariansFactory extends AbstractBuilderFactory {

    private int HMS;
    private double HMCR;
    private double PAR;
    private double BW;
    private double BWMin;
    private double BWMax;
    private double PARMax;
    private double PARMin;
    private double PM;

    public HSVariansFactory(AbstractParametersFactory parametersFactory) {
        super(parametersFactory);
    }

    @Override
    public AlgorithmBuilder getAlgorithm(String name, AbstractELMEvaluator.EvaluatorType evaluatorType, DoubleProblem problem) throws Exception {
        this.evaluatorType = evaluatorType;
        int evaluations = evaluatorType == AbstractELMEvaluator.EvaluatorType.TT ? EVALUATIONS_TT : EVALUATIONS_CV;
        AlgorithmBuilder builder = null;
        this.loadAlgorithmValues(name, evaluatorType);
        switch (name) {
            case "HS":
                builder = this.getHS(evaluations, problem);
                break;
            case "IHS":
                builder = this.getIHS(evaluations, problem);
                break;
            case "GHS":
                builder = this.getGHS(evaluations, problem);
                break;
            case "NGHS":
                builder = this.getNGHS(evaluations, problem);
                break;
            default:
                throw new JMetalException("Algorithm " + name + " not exists");
        }
        return builder;
    }

    @Override
    protected void loadAlgorithmValues(String name, AbstractELMEvaluator.EvaluatorType evaluatorType) throws Exception {
        HMS = (int) parametersFactory.getValue("HMS", evaluatorType, "HS");
        HMCR = parametersFactory.getValue("HMCR", evaluatorType, "HS");
        PAR = parametersFactory.getValue("PAR", evaluatorType, "HS");
        BW = parametersFactory.getValue("BW", evaluatorType, "HS");
        PARMax = parametersFactory.getValue("PARMax", evaluatorType, "IHS");
        PARMin = parametersFactory.getValue("PARMin", evaluatorType, "IHS");
        BWMax = parametersFactory.getValue("BWMax", evaluatorType, "IHS");
        BWMin = parametersFactory.getValue("BWMin", evaluatorType, "IHS");
        PM = parametersFactory.getValue("PM", evaluatorType, "NGHS");
    }

    public AlgorithmBuilder getHS(int evaluations, DoubleProblem problem) {
        return new HSBuilder(problem).setHMS(HMS).setHMCR(HMCR).setPAR(PAR).setBW(BW).setMaxEvaluations(evaluations);
    }

    public AlgorithmBuilder getIHS(int evaluations, DoubleProblem problem) {
        return new IHSBuilder(problem).setHMS(HMS).setHMCR(HMCR).setPARMAX(PARMax).setPARMIN(PARMin).setBWMAX(BWMax).setBWMIN(BWMin).setMaxEvaluations(evaluations);

    }

    public AlgorithmBuilder getGHS(int evaluations, DoubleProblem problem) {
        return new GHSBuilder(problem).setHMS(HMS).setHMCR(HMCR).setPARMAX(PARMax).setPARMIN(PARMin).setMaxEvaluations(evaluations);
    }

    public AlgorithmBuilder getNGHS(int evaluations, DoubleProblem problem) {
        return new NGHSBuilder(problem).setHMS(HMS).setPM(PM).setMaxEvaluations(evaluations);
    }
}
