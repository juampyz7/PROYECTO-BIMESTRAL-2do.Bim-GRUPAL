package com.streamflow.modelo;

public class Documental extends Contenido {

    private String tema;

    public Documental(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String tema) {
        super(id, titulo, genero, calidad, duracionMinutos);
        this.tema = tema;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo documental: " + titulo + " (" + calidad + ")";
    }

    @Override
    public String obtenerDetalles() {
        return "Documental: " + titulo + " | Tema: " + tema + " | Duracion: " + duracionMinutos
                + " min | Calidad: " + calidad;
    }

    @Override
    public double calcularFactorRecomendacion() {
        return 0.8;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
