package com.streamflow.modelo;

public class Pelicula extends Contenido {

    private String director;

    public Pelicula(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String director) {
        super(id, titulo, genero, calidad, duracionMinutos);
        this.director = director;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo pelicula: " + titulo + " (" + calidad + ")";
    }

    @Override
    public String obtenerDetalles() {
        return "Pelicula: " + titulo + " | Genero: " + genero + " | Director: " + director
                + " | Duracion: " + duracionMinutos + " min | Calidad: " + calidad;
    }

    @Override
    public double calcularFactorRecomendacion() {
        return duracionMinutos >= 150 ? 1.5 : 1.0;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
