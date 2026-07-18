package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Serie;

import java.util.LinkedHashMap;
import java.util.Map;

public class SerieMapeador implements ContenidoTipoMapeador {

    @Override
    public String obtenerTipo() {
        return "SERIE";
    }

    @Override
    public boolean soporta(Contenido contenido) {
        return contenido instanceof Serie;
    }

    @Override
    public String serializarAtributos(Contenido contenido) {
        Serie serie = (Serie) contenido;
        Map<String, String> atributos = new LinkedHashMap<>();
        atributos.put("temporadas", String.valueOf(serie.getTemporadas()));
        atributos.put("episodios", String.valueOf(serie.getEpisodiosPorTemporada()));
        return AtributosExtra.serializar(atributos);
    }

    @Override
    public Contenido construir(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String atributosExtra) {
        Map<String, String> atributos = AtributosExtra.deserializar(atributosExtra);
        int temporadas = Integer.parseInt(atributos.get("temporadas"));
        int episodios = Integer.parseInt(atributos.get("episodios"));
        return new Serie(id, titulo, genero, calidad, duracionMinutos, temporadas, episodios);
    }
}
