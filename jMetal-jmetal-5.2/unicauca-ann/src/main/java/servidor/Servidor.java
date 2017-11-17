package servidor;

import interfaces.Tarea;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.*;

public class Servidor {

    private static int PORT = 444;

    public static void main(String args[]) throws IOException {
        ServerSocket ss;
        BuilderTareas bt = new BuilderTareas();
        ArrayList<Tarea> tareas = new ArrayList<>();
        tareas = bt.getTareas();
        ArrayList algoritmos = new ArrayList();
        if (bt.getTypeValidacion().size() == 1) {//CV o TT
            algoritmos = bt.getAlgortimos();
        } else {
            ArrayList aux = bt.getAlgortimos();
            algoritmos = bt.getAlgortimos();
            for (Object alg : aux) {
                algoritmos.add(alg);
            }
        }
        if (bt.getRespresentacionSolucion().size() > 1) {
            ArrayList tmp = (ArrayList) algoritmos.clone();
            for (Object alg : tmp) {
                algoritmos.add(alg);
            }

        }

        algoritmos.add("last");
        System.out.print("Inicializando servidor... ");
      
        try {
            ss = new ServerSocket(PORT);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                ((ServidorHilo) new ServidorHilo(socket, idSession, tareas, algoritmos)).start();
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
