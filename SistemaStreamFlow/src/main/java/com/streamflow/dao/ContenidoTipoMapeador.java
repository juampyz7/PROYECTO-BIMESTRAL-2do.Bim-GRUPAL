package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

public interface ContenidoTipoMapeador {

    String obtenerTipo();

    boolean soporta(Contenido contenido);

    String serializarAtributos(Contenido contenido);

    Contenido construir(int id, String titulo, Genero genero, CalidadStreaming calidad, int duracionMinutos, String atributosExtra);
}
