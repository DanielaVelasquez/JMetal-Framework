package cliente;

import co.edu.unicauca.exec.cross_validation.harmony.ControladorExp;
import interfaces.Tarea;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class BuilderTarea extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    ReentrantLock lock = new ReentrantLock();

    private int port = 444;
    private String address = "127.0.0.1";
    private long id;

    public BuilderTarea(long id) {
        this.id = id;
    }

    public BuilderTarea(long id, int puerto, String address) {
        this.id = id;
        this.port = puerto;
        this.address = address;
    }

    @Override

    public void run() {
        try {
            boolean builtarea = true;
            sk = new Socket(address, port);
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());
            while (builtarea) {
                try {

                    String tareasTexto = dis.readUTF();

                    if (tareasTexto.contains("Sin tareas")) {
                        System.out.println("cliente " + id + " " + tareasTexto);
                        //shutdown PC
                        builtarea = false;
                    } else {
                        Tarea t = new Tarea(tareasTexto);
                        tareasTexto = procesar(t);
                        System.out.println("cliente " + id + " " + t.toString() + "," + t.getAlgorimo() + "," + t.getId());

                    }
                    dos.writeUTF(tareasTexto + ",Cliente " + id + " result ");

                } catch (IOException ex) {
                    dis.close();
                    dos.close();
                    sk.close();
                    run();
                }
            }
            dis.close();
            dos.close();
            sk.close();
        } catch (IOException ex) {

            System.out.println("Error de Sk");

        }

    }

    public String procesar(Tarea T) {
        lock.lock();
        try {
            String comandoCMD = "-p " + T.getProblemName() + " -a " + T.getAlgorimo() + " -T " + T.getAlgorimo() + "parametros.txt";
            System.out.println("" + comandoCMD);
            String[] args = comandoCMD.split(" ");
            ControladorExp exp = new ControladorExp();
            exp.setTarea(T);
            try {
                exp.ejecutar(args);
            } catch (IOException e) {

            }
            T = exp.getTarea();
        } finally {
            lock.unlock();
        }

        return T.toString();
    }

}

public class Cliente {

    public static void main(String[] args) {
        /*
        System.out.println("Prueba tarea");
        Tarea t=new Tarea("5,0.14999999999999997,0.31999999999999995,4609.0,1,1,Iris,HS,0,CV,1,75");
        BuilderTarea b=new BuilderTarea(0);
        System.out.println(""+ b.procesar(t));*/

        int puerto = 444;
        String address = "localhost";
        if (args.length > 0) {
            puerto = Integer.parseInt(args[1]);
            address = args[3];
            System.out.println("Conexion con " + address + " Port : " + puerto);
        }
        ArrayList<Thread> clients = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            clients.add(new BuilderTarea(i, puerto, address));
        }
        for (Thread thread : clients) {
            thread.start();
        }
    }
}
