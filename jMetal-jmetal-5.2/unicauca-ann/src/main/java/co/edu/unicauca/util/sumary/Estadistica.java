package co.edu.unicauca.util.sumary;

public class Estadistica {

    public double desviacion(float[] valor) {
        float media;
        float varianza =  0.0000f;
        float desviacion;
        varianza = 0.0f;
        desviacion = 0.0f;
        int num = valor.length;

        media = media(valor);
        for (int i = 0; i < num; i++) {
            float rango;
            rango = (float) Math.pow(valor[i] - media, 2);
            varianza = varianza + rango;
        }
        varianza = varianza / (num);//sobre n

        desviacion = (float) Math.sqrt(varianza);
        if (Double.isNaN(desviacion)) {
            desviacion = 0.0f;
        }
        return desviacion;

    }

    public float media(float[] values) {
        float value = 0.0f;
        float sum = sum(values);
        value = (float) (sum / values.length);
        return value;
    }

    public float sum(float[] values) {
        int num = values.length;
        float sum = 0.0f;
        for (int i = 0; i < num; i++) {
            sum += values[i];
        }
        return sum;
    }
}
