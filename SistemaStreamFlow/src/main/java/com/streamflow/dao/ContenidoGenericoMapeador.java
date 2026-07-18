package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.ContenidoGenerico;
import com.streamflow.modelo.Genero;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContenidoGenericoMapeador implements ContenidoTipoMapeador {

    @Override
    public String obtenerTipo() {
        return "GENERICO";
    }

    @Override
    public boolean soporta(Contenido contenido) {
        return contenido instanceof ContenidoGenerico;
    }

    @Override
    public String serializarAtributos(Contenido contenido) {
        ContenidoGenerico generico = (ContenidoGenerico) contenido;
        Map<String, String> atributos = new LinkedHashMap<>();
        atributos.put("tipoPersonalizado", generico.getTipoPersonalizado());
        atributos.putAll(generico.getAtributosPersonalizados());
        return AtributosExtra.serializar(atributos);
    }

    @Override
    public Contenido construir(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String atributosExtra) {
        Map<String, String> atributos = new LinkedHashMap<>(AtributosExtra.deserializar(atributosExtra));
        String tipoPersonalizado = atributos.remove("tipoPersonalizado");
        return new ContenidoGenerico(id, titulo, genero, calidad, duracionMinutos, tipoPersonalizado, atributos);
    }
}
