package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;

import java.util.Map;

public class PeliculaMapeador implements ContenidoTipoMapeador {

    @Override
    public String obtenerTipo() {
        return "PELICULA";
    }

    @Override
    public boolean soporta(Contenido contenido) {
        return contenido instanceof Pelicula;
    }

    @Override
    public String serializarAtributos(Contenido contenido) {
        Pelicula pelicula = (Pelicula) contenido;
        return AtributosExtra.serializar(Map.of("director", pelicula.getDirector()));
    }

    @Override
    public Contenido construir(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String atributosExtra) {
        Map<String, String> atributos = AtributosExtra.deserializar(atributosExtra);
        return new Pelicula(id, titulo, genero, calidad, duracionMinutos, atributos.get("director"));
    }
}
