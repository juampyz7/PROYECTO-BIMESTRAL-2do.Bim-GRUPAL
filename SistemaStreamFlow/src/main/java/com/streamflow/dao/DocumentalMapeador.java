package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Genero;

import java.util.Map;

public class DocumentalMapeador implements ContenidoTipoMapeador {

    @Override
    public String obtenerTipo() {
        return "DOCUMENTAL";
    }

    @Override
    public boolean soporta(Contenido contenido) {
        return contenido instanceof Documental;
    }

    @Override
    public String serializarAtributos(Contenido contenido) {
        Documental documental = (Documental) contenido;
        return AtributosExtra.serializar(Map.of("tema", documental.getTema()));
    }

    @Override
    public Contenido construir(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String atributosExtra) {
        Map<String, String> atributos = AtributosExtra.deserializar(atributosExtra);
        return new Documental(id, titulo, genero, calidad, duracionMinutos, atributos.get("tema"));
    }
}
