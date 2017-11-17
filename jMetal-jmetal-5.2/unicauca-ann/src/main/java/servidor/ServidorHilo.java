package servidor;

import co.edu.unicauca.exec.cross_validation.harmony.CVExperiment;
import co.edu.unicauca.util.sumary.SendFileEmail;
import co.edu.unicauca.util.sumary.Zipper;
import interfaces.Tarea;
import java.io.*;
import java.net.*;
import java.util.logging.*;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ServidorHilo extends Thread {
    
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    public ArrayList<Tarea> tareas;
    public ArrayList algoritmos;
    ReentrantLock lock = new ReentrantLock();
    
    public ServidorHilo(Socket socket, int id, ArrayList<Tarea> tareas, ArrayList algoritmos) {
        this.socket = socket;
        this.idSessio = id;
        this.tareas = tareas;
        this.algoritmos = algoritmos;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desconnectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        boolean exitTaskUnassigend = false;
        boolean haveTask = true;
        boolean sendMailResults = false;
        boolean takeTaks = false;
        Tarea aux = null;
        
        while (haveTask) {
            takeTaks = false;
            try {
                while (!takeTaks) {
                    lock.lock();
                    try {
                        if (tareas.isEmpty()) {
                            System.out.println("Sin tareas finished");
                            takeTaks = true;
                            haveTask = false;
                            exitTaskUnassigend = false;
                            sendMailResults = false;
                            
                        } else {
                            tareas.get(0).setAssigned(1);
                            aux = new Tarea(tareas.get(0).toString());
                            
                            if (algoritmos.size() > 0) {
                                if (!aux.getAlgorimo().equalsIgnoreCase(algoritmos.get(0) + "")) {
                                    sendMailResults = true;
                                    System.out.println(" " + algoritmos.get(0));
                                }
                            } else {
                                sendMailResults = true;
                            }
                            
                            if (sendMailResults) {
                                if (algoritmos.size() > 0) {
                                    algoritmos.remove(0);
                                }
                            }
                            tareas.remove(0);
                            exitTaskUnassigend = true;
                            takeTaks = true;
                        }
                        
                    } catch (Exception e) {
                        takeTaks = true;//To break While
                        haveTask = false;
                        exitTaskUnassigend = false;
                        e.printStackTrace();
                        break;
                    } finally {
                        lock.unlock();
                    }
                }
                
                if (exitTaskUnassigend) {
                    dos.writeUTF(aux + ",Tareas [" + aux.getSeed() + "]");
                    exitTaskUnassigend = false;
                    String accion = "";
                    accion = dis.readUTF();
                    if (accion.contains("result")) {
                        aux = new Tarea(accion);
                        write(aux, sendMailResults);
                    }
                } else {
                    if (tareas.isEmpty()) {
                        dos.writeUTF("Sin tareas :");
                        haveTask = false;
                        if (sendMailResults) {
                            sendMail();
                        }
                    }
                }
                sendMailResults = false;
                exitTaskUnassigend = false;
                
            } catch (IOException ex) {
                writeErrorTarea(null, aux);
                sendMailResults = false;
                exitTaskUnassigend = false;
                break;
            }
            sendMailResults = false;
            
        }
        System.out.println(" " + this.getName() + " finished " + this.getName() + " Nuber of tareas: " + tareas.size());
        desconnectar();
    }
    
    public void write(Tarea t, boolean sendMailResults) {
        
        boolean writed = false;
        while (!writed) {
            lock.lock();
            
            try {
                if (t.getTrain() > -1 && t.getState() == 1) {
                    writeTaskOnPath("null", t);
                } else {
                    writeErrorTarea(null, t);
                }
                
                writed = true;
            } catch (Exception e) {
                writed = false;
                System.out.println("Dont write");
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            if (sendMailResults) {
                sendMail();
            }
            
        }
    }
    
    public void writeTaskOnPath(String path, Tarea t) {
        boolean write = false;
        
        while (!write) {
            try {
                lock.lock();
                try {
                    Object resultForFile = t.getProblemName();
                    String directorio = "Result\\" + t.getAlgorimo() + "Result\\s" + t.getTypeSolution() + "\\" + t.getTypeValidation() + "\\";
                    String SumaryNameFile = directorio + "c" + t.getConfiguracion() + "_" + resultForFile + "_Results.txt";
                    File dir = new File(directorio);
                    
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    
                    BufferedWriter sumary;
                    try {
                        sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
                        sumary.newLine();
                        t.setState(1);
                        sumary.write(t.toString());
                        sumary.close();
                        write = true;
                      //---  writeTareaEchas(t);
                    } catch (IOException ex) {
                        Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                System.out.println("****dont write: " + t.toString());
                write = false;
            }
        }
        
    }
    
    public void writeTareaEchas(Tarea t) {
        String SumaryNameFile = "echas.txt";
        boolean writed = false;
        BufferedWriter sumary;
        
        while (!writed) {
            lock.lock();
            try {
                sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
                sumary.newLine();
                t.setState(1);
                sumary.write(t.toString());
                sumary.close();
                writed = true;
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class
                        .getName()).log(Level.SEVERE, null, ex);
            } finally {
                lock.unlock();
            }
        }
    }
    
    public void writeErrorTarea(String path, Tarea t) {
        
        boolean write = false;
        while (!write) {
            lock.lock();
            try {
                String directorio = "Result\\";
                String SumaryNameFile = directorio + "Error.txt";
                
                File dir = new File(directorio);
                
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                BufferedWriter sumary;
                try {
                    sumary = new BufferedWriter(new FileWriter(SumaryNameFile, true));
                    sumary.newLine();
                    t.setState(1);
                    sumary.write(t.toString());
                    sumary.close();
                    
                } catch (IOException ex) {
                    Logger.getLogger(ServidorHilo.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                write = true;
            } catch (Exception e) {
                System.out.println("****dont write Error: " + t.toString());
                write = false;
                
            } finally {
                lock.unlock();
            }
        }
        
    }
    
    public void sendMail() {
        lock.lock();
        try {
            System.out.println("send result by : " + idSessio + " " + this.getName());
            comprimir();
            sendMail("Result: " + idSessio, "Result.zip", "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        
    }
    
    public static void sendMail(String resultsName, String filename, String message) {
        SendFileEmail sender = new SendFileEmail();
        sender.send(resultsName, filename, message);
    }
    
    public void comprimir() {
        try {
            Zipper zp = new Zipper();
            zp.comprimir_carpeta("Result", "Result.zip");
            
        } catch (Exception ex) {
            Logger.getLogger(CVExperiment.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
