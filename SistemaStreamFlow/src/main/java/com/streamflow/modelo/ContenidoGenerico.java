package com.streamflow.modelo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContenidoGenerico extends Contenido {

    private String tipoPersonalizado;
    private Map<String, String> atributosPersonalizados;

    public ContenidoGenerico(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos,
                              String tipoPersonalizado, Map<String, String> atributosPersonalizados) {
        super(id, titulo, genero, calidad, duracionMinutos);
        this.tipoPersonalizado = tipoPersonalizado;
        this.atributosPersonalizados = new LinkedHashMap<>(atributosPersonalizados);
    }

    @Override
    public String reproducir() {
        return "Reproduciendo " + tipoPersonalizado + ": " + titulo + " (" + calidad + ")";
    }

    @Override
    public String obtenerDetalles() {
        StringBuilder detalles = new StringBuilder();
        detalles.append(tipoPersonalizado).append(": ").append(titulo)
                .append(" | Genero: ").append(genero);
        for (Map.Entry<String, String> atributo : atributosPersonalizados.entrySet()) {
            detalles.append(" | ").append(atributo.getKey()).append(": ").append(atributo.getValue());
        }
        detalles.append(" | Duracion: ").append(duracionMinutos)
                .append(" min | Calidad: ").append(calidad);
        return detalles.toString();
    }

    @Override
    public double calcularFactorRecomendacion() {
        return 0.7;
    }

    public String getTipoPersonalizado() {
        return tipoPersonalizado;
    }

    public Map<String, String> getAtributosPersonalizados() {
        return atributosPersonalizados;
    }
}
