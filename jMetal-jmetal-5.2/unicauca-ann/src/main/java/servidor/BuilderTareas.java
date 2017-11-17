package servidor;

import interfaces.Tarea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.util.CMAESUtils;

public class BuilderTareas {

    private int MAXEXPERIMENT = 35;

    public ArrayList<Tarea> getTareas() {
        ArrayList<Tarea> tareasPorHacer = new ArrayList();
        ArrayList algoritmos = getAlgortimos();
        ArrayList problemas = getProbremas();
        ArrayList tiposValidadcion = getTypeValidacion();
        ArrayList tiposSolucion = getRespresentacionSolucion();

        ArrayList<Tarea> cmes = new ArrayList<>();
        int idTaks = 0;

        for (Object tipoSolucion : tiposSolucion) {
            for (Object tipoValidacion : tiposValidadcion) {
                for (Object algoritmo : algoritmos) {
                    try {
                        ArrayList<ArrayList> parametersFile = readParameter(algoritmo + "parametros.txt");
                        for (int k = 0; k < parametersFile.size(); k++) {
                            for (Object problema : problemas) {
                                for (int i = 0; i < MAXEXPERIMENT; i++) {
                                    Tarea tarea = new Tarea(i, -1, -1, -1, 0, 0, problema + "", algoritmo + "", k, tipoValidacion + "", Integer.parseInt(tipoSolucion + ""), idTaks);
                                    idTaks++;
                                    if (!verificarEcha(tarea)) {
                                        if ("cmaes".equalsIgnoreCase(tarea.getAlgorimo()) && "TT".equalsIgnoreCase(tarea.getTypeValidation())) {
                                            cmes.add(tarea);
                                        } else {
                                            tareasPorHacer.add(tarea);
                                        }
                                    }
                                }
                            }
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(BuilderTareas.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }

        for (Tarea t : cmes) {
            tareasPorHacer.add(t);
        }

        System.out.println("Numero de tareas por hacer: " + tareasPorHacer.size());
        return tareasPorHacer;
    }

    public ArrayList<Tarea> TasksFromFile(String filename) {
        ArrayList<Tarea> lst = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {

            ArrayList<ArrayList> parameters = new ArrayList<>();
            try {
                fr = new FileReader(filename);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuilderTareas.class.getName()).log(Level.SEVERE, null, ex);
            }
            br = new BufferedReader(fr);
            String actualLine;

            while ((actualLine = br.readLine()) != null) {

                if (actualLine.contains(",")) {//Is a line with parameters
                    Tarea echa = new Tarea(actualLine);
                    lst.add(echa);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(BuilderTareas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lst;
    }

    public ArrayList<Tarea> compara(ArrayList<Tarea> echas, ArrayList<Tarea> porHacer, int Echas) {
        System.out.println("Start comparingt");
        ArrayList<Tarea> paraHacer = new ArrayList();
        boolean tareaEcha = false;
        for (int i = 0; i < Echas; i++) {
            porHacer.remove(0);
        }
        for (Tarea t : porHacer) {
            paraHacer.add(t);
        }
        /*for (Tarea t : porHacer) {
            tareaEcha = false;
            for (Tarea echa : echas) {
                if (t.getId() == echa.getId()) {
                    tareaEcha = true;
                    break;
                }
            }
            if (!tareaEcha) {
                paraHacer.add(t);

            }
        }*/
        return paraHacer;
    }

    public ArrayList getAlgortimos() {
        ArrayList algList = new ArrayList();

        algList.add("HS");
        algList.add("GHS");
        algList.add("IHS");
        algList.add("NGHS");
        algList.add("cmaes");
        algList.add("M_ELM");
        algList.add("PSO");
        algList.add("DEExp");
        algList.add("DEBin");
        return algList;

    }

    public ArrayList getProbremas() {
        ArrayList probList = new ArrayList<>();

        probList.add("Iris");
        probList.add("Vertebral2C");
        probList.add("Dermatology");
        probList.add("Banknote");
        probList.add("Blood");
        probList.add("Cardiotocography");
        probList.add("Car");
        probList.add("Chart");
        probList.add("Connectionist");
        probList.add("Contraceptive");
        probList.add("Dermatology");
        probList.add("Diabetes");
        probList.add("Ecoli");
        probList.add("Fertility");
        probList.add("Glass");
        probList.add("Haberman");
        probList.add("Hayes");
        probList.add("Hill");
        probList.add("Indian");
        probList.add("Ionosphere");
        probList.add("Leaf");
        probList.add("Letter");
        probList.add("Libras");
        probList.add("Optdigits");
        probList.add("Seeds");
        probList.add("SPECTF");
        probList.add("Vertebral2C");
        probList.add("Vertebral3C");
        probList.add("Pen");
        probList.add("QSARBiodegradation");
        probList.add("Wdbc");
        probList.add("Yeast");
        probList.add("Wine");
        probList.add("Zoo");
        //Regression problemas
        probList.add("AutoMpg");
        probList.add("AutoPrice");
        probList.add("Cpu");
        probList.add("Sensory");
        probList.add("Servo");
        probList.add("Veteran");
        probList.add("Housing");
        probList.add("BodyFat");

        return probList;
    }

    public ArrayList getRespresentacionSolucion() {
        ArrayList typeSol = new ArrayList<>();
        typeSol.add(0);//vectoe=[peso,bias,peso,bias....]
        typeSol.add(1);//vector=[peso,peso,peso,bias.....]
        return typeSol;
    }

    public ArrayList<ArrayList> readParameter(String fileName) throws FileNotFoundException, IOException {
        BufferedReader br = null;
        FileReader fr = null;

        ArrayList<ArrayList> parameters = new ArrayList<>();
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        String actualLine;

        while ((actualLine = br.readLine()) != null) {
            String[] aux = actualLine.split(" ");
            if (!aux[0].contains("#")) {//Is a line with parameters
                ArrayList lineParametros = new ArrayList<>();
                for (String value : aux) {
                    lineParametros.add(value);
                }
                parameters.add(lineParametros);
            }
            aux = null;
        }

        return parameters;
    }

    public ArrayList getTypeValidacion() {
        ArrayList tipos = new ArrayList();
        tipos.add("CV");
        tipos.add("TT");
        return tipos;
    }

    public void writeTask(ArrayList<Tarea> task) {

        try {
            String directorio = "";
            String SumaryNameFile = directorio + "Tareas.txt";

            File dir = new File(directorio);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            BufferedWriter sumary;
            try {
                sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
                for (Tarea t : task) {
                    sumary.newLine();
                    sumary.write(t.toString());
                }
                sumary.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean verificarEcha(Tarea t) {

        Object resultForFile = t.getProblemName();
        String directorio = "Result\\" + t.getAlgorimo() + "Result\\s" + t.getTypeSolution() + "\\" + t.getTypeValidation() + "\\";
        String SumaryNameFile = directorio + "c" + t.getConfiguracion() + "_" + resultForFile + "_Results.txt";
        File dir = new File(directorio);
        ArrayList<Tarea> lst = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;

        ArrayList<ArrayList> parameters = new ArrayList<>();
        try {
            fr = new FileReader(SumaryNameFile);
        } catch (FileNotFoundException ex) {
            return false;
        }
        br = new BufferedReader(fr);
        String actualLine;

        boolean echaT = false;
        try {
            while ((actualLine = br.readLine()) != null) {
                echaT = false;
                if (actualLine.contains(",")) {//Is a line with parameters
                    Tarea echa = new Tarea(actualLine);
                    if (echa.getSeed() == t.getSeed() && echa.getConfiguracion() == t.getConfiguracion()) {
                        echaT = true;
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(BuilderTareas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return echaT;
    }

}
