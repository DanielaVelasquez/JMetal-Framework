package co.edu.unicauca.util.sumary;

public class Estadistica {

    private double sumatoria = 0;
    private double media;
    private double varianza = 0.0f;
    private double desviacion;

    public double desviacion(double[] valor) {

        varianza = 0.0f;
        media = 0.0f;
        sumatoria = 0.0f;
        desviacion = 0.0f;
        int num = valor.length;
        media = media(valor);
        for (int i = 0; i < num; i++) {
            double rango;
            rango = Math.pow(valor[i] - media, 2);
            varianza = varianza + rango;
        }
        varianza = varianza / (num);//sobre n

        desviacion = Math.sqrt(varianza);
        if (Double.isNaN(desviacion)) {
            desviacion = 0.0;
        }
        return desviacion;

    }

    public double media(double[] valor) {
        int num = valor.length;
        for (int i = 0; i < num; i++) {
            sumatoria = sumatoria + (double) valor[i];
        }
        media = sumatoria / (num * 1.0f);
        return media;
    }

    public double distribucionNormal(double x, double a, double b) {

        double distriNormal = 0;
        //numerador    
        double a1, a2, a3;
        a1 = (x - a) * (x - a);
        a2 = 2 * (b * b);
        a3 = a1 / a2;

        //denominador
        double b1, b2, b3;
        b1 = 2 * Math.PI;
        b2 = Math.sqrt(b1);
        b3 = b2 * b;

        //calculo
        double c1, c2;
        c1 = Math.pow(Math.E, -a3);
        distriNormal = c1 / b3;
        // System.out.println("distribucion normal "+distriNormal);
        return distriNormal;
    }

    public double getSumatoria() {
        return sumatoria;
    }

    public double getMedia() {
        return media;
    }

    public double getVarianza() {
        return varianza;
    }

    public double getDesviacion() {
        return desviacion;
    }

}
