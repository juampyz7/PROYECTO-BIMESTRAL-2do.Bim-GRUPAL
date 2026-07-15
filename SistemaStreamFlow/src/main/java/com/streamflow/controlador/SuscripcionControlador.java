package com.streamflow.controlador;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.servicio.SuscripcionServicio;

import java.util.List;

public class SuscripcionControlador {

    private final SuscripcionServicio suscripcionServicio;

    public SuscripcionControlador(SuscripcionServicio suscripcionService) {
        this.suscripcionServicio = suscripcionService;
    }

    public Suscripcion registrarSuscripcion(int id, int usuarioId, CalidadStreaming calidad) {
        return suscripcionServicio.crearSuscripcion(id, usuarioId, calidad);
    }

    public List<Suscripcion> obtenerSuscripciones(int usuarioId) {
        return suscripcionServicio.obtenerSuscripcionesDeUsuario(usuarioId);
    }

    public void cancelarSuscripcion(Suscripcion suscripcion) {
        suscripcionServicio.cancelarSuscripcion(suscripcion);
    }
}