package co.edu.unicauca.exec.cross_validation.harmony;

import interfaces.Tarea;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Monitor admin
 */
public class ControladorExp {

    ArrayList algoritmosLst = new ArrayList();
    ArrayList problemasLst = new ArrayList();
    ArrayList parametrosLst = new ArrayList();
    Tarea tarea = null;

    public void ejecutar(String[] args) throws IOException {
        /*
        args = "-p Iris Leaf -A DEbin -T DEbinparametros.txt".split(" ");
        args = "-p Iris -A DEexp -T DEexpparametros.txt".split(" ");
        args = "-p Iris -A ghs -T ghsparametros.txt".split(" ");
        args = "-p Iris -A ihs -T ihsparametros.txt".split(" ");
        args = "-p Iris -A nghs -T nghsparametros.txt".split(" ");
        args = "-p Iris -A m_elm -T m_elmparametros.txt".split(" ");
        // args = "-p Iris -A pso -T psoparametros.txt".split(" ");//problemas en los selectore corregir
        args = "-p Iris Leaf -A DEbin -T DEbinparametros.txt".split(" ");*/
        // args = "-p Iris -A hs -T hsparametros.txt".split(" ");
        // args = "-p Banknote -A pso -T psoparametros.txt".split(" ");//problemas en los selectore corregir
        //  args = "-p Iris -A cmaes -T cmaesparametros.txt".split(" ");
        if (args.length > 0) {
            leerCMD(args);
            //System.out.println("" + algoritmosLst.toString());
            // System.out.println("" + problemasLst.toString());
        }

        String paquete = "co.edu.unicauca.problem.cross_validation.";
        CVExperiment exp = new CVExperiment();

        if (tarea != null) {
            if (tarea.getTypeValidation().equalsIgnoreCase("CV")) {
                paquete = "co.edu.unicauca.problem.cross_validation.";
            } else {
                paquete = "co.edu.unicauca.problem.training_testing.";
                exp.setDEFAULT_EFOS(exp.getDEFAULT_EFOS() * 10);
            }
        }
        ArrayList parameters = null;

        for (int i = 1; i < problemasLst.size(); i++) {
            for (int j = 1; j < algoritmosLst.size(); j++) {
                if (parametrosLst.size() > 0) {
                    ArrayList<ArrayList> parametersFile = readParameter((String) algoritmosLst.get(j) + "parametros.txt");
                    ///ArrayList<ArrayList> parametersFile = readParameter("hsparametros.txt");
                    exp.setTarea(tarea);
                    if (tarea != null) {
                        parameters = parametersFile.get(tarea.getConfiguracion());
                        builderAlg(exp, paquete, i, j, tarea.getConfiguracion(), parameters);
                    } else {
                        for (int k = 0; k < parametersFile.size(); k++) {
                            parameters = parametersFile.get(k);

                            builderAlg(exp, paquete, i, j, k, parameters);
                        }
                    }
                    exp.setTarea(null);
                }
                System.gc();
            }
        }
        // exp.sendResult(problemasLst.toString() + "  " + algoritmosLst.toString() + "\n " + parametrosLst.toString());
        System.gc();

    }

    public void builderAlg(CVExperiment exp, String paquete, int i, int j, int k, ArrayList parameters) {
        String indicator = "c";
        if ("pso".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.PSO(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("hs".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.HS(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("ghs".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.GHS(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("ihs".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.IHS(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("nghs".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.NGHS(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("DEexp".equalsIgnoreCase(algoritmosLst.get(j) + "") || "Debin".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.DE(paquete + problemasLst.get(i), parameters, algoritmosLst.get(j) + indicator + k);
        } else if ("cmaes".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.CMAES(paquete + problemasLst.get(i), parameters, indicator + k);
        } else if ("m_elm".equalsIgnoreCase(algoritmosLst.get(j) + "")) {
            exp.MA_ELM(paquete + problemasLst.get(i), parameters, indicator + k);
        } else {
            System.out.println("Algorithms no Found " + tarea);
        }

    }

    public void leerCMD(String[] args) {
        boolean algoritmo = false;
        boolean parametro = false;
        boolean problema = false;
        for (String p : args) {
            if (p.contains("-")) {
                if ("-p".equalsIgnoreCase(p)) {
                    problema = true;
                    algoritmo = false;
                    parametro = false;
                } else if ("-a".equalsIgnoreCase(p)) {
                    problema = false;
                    algoritmo = true;
                    parametro = false;
                } else {
                    problema = false;
                    algoritmo = false;
                    parametro = true;
                }
            }
            if (algoritmo) {
                algoritmosLst.add(p);
            } else if (problema) {
                problemasLst.add(p);
            } else if (parametro) {
                parametrosLst.add(p);
            }
        }
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

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

}
