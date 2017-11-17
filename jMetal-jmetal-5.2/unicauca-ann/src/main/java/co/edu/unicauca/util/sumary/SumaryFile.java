package co.edu.unicauca.util.sumary;

import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.LocalSearchOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

public class SumaryFile {

    public static void SumaryHS(Algorithm MyAlgoritm, LocalSearchOperator LS, AbstractELMEvaluator evaluator, List<DoubleSolution> Solutions, List AccList, List time, Problem problema, String descriptor) throws IOException {

        float[] auxTrainHist = new float[Solutions.size()];
        float[] auxTestHist = new float[Solutions.size()];
        float[] auxTime = new float[Solutions.size()];
        float best = (float)Solutions.get(0).getObjective(0);
        String words = " ";

        Object resultForFile = evaluator.getProblemName();
        String directorio = "Result\\" + MyAlgoritm.getName() + "Result\\" + evaluator.getType() + "\\";

        String SumaryNameFile = directorio + descriptor +"_"+ resultForFile + "_Results.txt";

        File dir = new File(directorio);
        

        if (!dir.exists()) {
            dir.mkdirs();
        }

        BufferedWriter sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
        System.out.println(SumaryNameFile);

        Date currentDate = new Date();

        sumary.newLine();
        sumary.newLine();
        for (int i = 0; i < 20; i++) {
            sumary.write("-----");
        }
        sumary.newLine();
        sumary.write(currentDate.toString());
        sumary.newLine();

        sumary.write(MyAlgoritm.getName());
        sumary.newLine();
        String parametersHS;

        parametersHS = MyAlgoritm.getDescription() + " maxExecution " + Solutions.size();
        String parametersOptimizador = " ";

        String parametersEvaluator = evaluator.getProblemName() + " Type " + evaluator.getType();

        if (LS != null) {
            parametersOptimizador = "Optimizador " + LS.toString() + " iteraciones " + LS.getNumberOfImprovements();
        }

        String parametersELM;
        parametersELM = " NHiddenNeurons " + evaluator.getElm().getHiddenNeurons() + " NInputNeuros " + evaluator.getElm().getInputNeurons() + " func " + "Null";
        sumary.write(parametersHS);
        sumary.newLine();

        sumary.write(parametersELM);
        sumary.newLine();

        sumary.write(parametersEvaluator);
        sumary.newLine();

        sumary.write(parametersOptimizador);
        sumary.newLine();

        sumary.write("TrainingAccuracyHistory      TestingAccuracyHistory");
        sumary.newLine();

        for (int i = 0; i < Solutions.size(); i++) {
            String line = "     Train [ " + i + "] = " + Solutions.get(i).getObjective(0) + " ";
            line = line + "  Test[" + i + "]=" + AccList.get(i);
            line += "   Time : " + time.get(i);
            System.out.println(line);
            auxTrainHist[i] = (float) Solutions.get(i).getObjective(0);
            double tmp = (double) AccList.get(i);
            auxTestHist[i] = (float) tmp;
            auxTime[i] = (long) time.get(i);

            if (Solutions.get(i).getObjective(0) > best) {
                best = (float)Solutions.get(i).getObjective(0);
            }
            sumary.write(line);
            sumary.newLine();

        }

        Estadistica st = new Estadistica();
        sumary.write(words);
        if (Solutions.size() > 1) {
            sumary.write("Train average: " + st.media(auxTrainHist));

            sumary.newLine();
            sumary.write(" Train Standard deviation: " + st.desviacion(auxTrainHist));

            sumary.newLine();
        }

        if (Solutions.size() > 1) {
            sumary.write(" Test average: " + st.media(auxTestHist));
            sumary.newLine();

            sumary.write(" Test Standard deviation: " + st.desviacion(auxTestHist));
            sumary.newLine();
        }
        sumary.write("Average running time: " + st.media(auxTime) + " ms");
        sumary.close();
        //sendMail(MyAlgoritm.getName()+"_" + resultForFile , SumaryNameFile, "Resultado para " + resultForFile + " en " + MyAlgoritm.getName());

    }

    private static String truncateTo(double unroundedNumber, int decimalPlaces) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (Double.isNaN(unroundedNumber)) {
            return "0.00000000";
        }
        numberFormat.setMaximumFractionDigits(decimalPlaces + 4);
        numberFormat.setMinimumFractionDigits(decimalPlaces + 4);
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        return numberFormat.format(unroundedNumber).replaceAll(",", ".");
    }

    
    
}
