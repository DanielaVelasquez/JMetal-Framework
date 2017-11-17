package co.edu.unicauca.util.sumary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    public void comprimir_carpeta(String origen, String destino) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destino));
        añadir_carpeta_al_zip("", origen, zip);
        zip.flush();
        zip.close();
    }

    public void añadir_archivo_al_zip(String path, String fichero, ZipOutputStream zip) throws Exception {

        File archivo = new File(fichero);

        if (archivo.isDirectory()) {
            añadir_carpeta_al_zip(path, fichero, zip);

        } else {

            byte[] buffer = new byte[4096];
            int leido;

            FileInputStream entrada = new FileInputStream(archivo);
            zip.putNextEntry(new ZipEntry(path + "/" + archivo.getName()));

            while ((leido = entrada.read(buffer)) > 0) {
                zip.write(buffer, 0, leido);
            }
        }
    }

    public void añadir_carpeta_al_zip(String path, String carpeta, ZipOutputStream zip) throws Exception {
        File directorio = new File(carpeta);
        for (String nombre_archivo : directorio.list()) {
            if (path.equals("")) {
                añadir_archivo_al_zip(directorio.getName(), carpeta + "/" + nombre_archivo, zip);
            } else {
                añadir_archivo_al_zip(path + "/" + directorio.getName(), carpeta + "/" + nombre_archivo, zip);
            }
        }
    }

}
