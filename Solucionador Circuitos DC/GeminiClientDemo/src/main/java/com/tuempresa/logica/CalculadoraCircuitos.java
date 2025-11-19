package com.tuempresa.logica;

public class CalculadoraCircuitos {

    public static double calcularSerie(double[] resistencias) {
        double total = 0;
        for (double r : resistencias) total += r;
        return total;
    }

    public static double calcularParalelo(double[] resistencias) {
        double inversa = 0;
        for (double r : resistencias) inversa += 1.0 / r;
        return 1.0 / inversa;
    }

    public static double calcularCorriente(double voltaje, double resistencia) {
        return voltaje / resistencia;
    }
}

