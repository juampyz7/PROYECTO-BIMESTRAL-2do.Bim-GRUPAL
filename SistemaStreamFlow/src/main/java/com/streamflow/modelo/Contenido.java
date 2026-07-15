package com.streamflow.modelo;

public abstract class Contenido {

    protected int id;
    protected String titulo;
    protected Genero genero;
    protected CalidadStreaming calidad;
    protected int duracionMinutos;

    public Contenido(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.calidad = calidad;
        this.duracionMinutos = duracionMinutos;
    }

    public abstract String reproducir();

    public abstract String obtenerDetalles();

    public abstract double calcularFactorRecomendacion();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public CalidadStreaming getCalidad() {
        return calidad;
    }

    public void setCalidad(CalidadStreaming calidad) {
        this.calidad = calidad;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
}
