package com.streamflow.modelo;

public class Serie extends Contenido {

    private int temporadas;
    private int episodiosPorTemporada;

    public Serie(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos,
                 int temporadas, int episodiosPorTemporada) {
        super(id, titulo, genero, calidad, duracionMinutos);
        this.temporadas = temporadas;
        this.episodiosPorTemporada = episodiosPorTemporada;
    }

    @Override
    public String reproducir() {
        return "Reproduciendo serie: " + titulo + " temporada 1 episodio 1 (" + calidad + ")";
    }

    @Override
    public String obtenerDetalles() {
        return "Serie: " + titulo + " | Genero: " + genero + " | Temporadas: " + temporadas
                + " | Episodios por temporada: " + episodiosPorTemporada + " | Calidad: " + calidad;
    }

    @Override
    public double calcularFactorRecomendacion() {
        return temporadas * 0.5;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    public int getEpisodiosPorTemporada() {
        return episodiosPorTemporada;
    }

    public void setEpisodiosPorTemporada(int episodiosPorTemporada) {
        this.episodiosPorTemporada = episodiosPorTemporada;
    }
}
