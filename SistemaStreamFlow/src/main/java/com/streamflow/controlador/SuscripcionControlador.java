package com.streamflow.controlador;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.servicio.SuscripcionServicio;

import java.util.List;

public class SuscripcionControlador {

    private final SuscripcionServicio suscripcionService;

    public SuscripcionControlador(SuscripcionServicio suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    public Suscripcion registrarSuscripcion(int id, int usuarioId, CalidadStreaming calidad) {
        return suscripcionService.crearSuscripcion(id, usuarioId, calidad);
    }

    public List<Suscripcion> obtenerSuscripciones(int usuarioId) {
        return suscripcionService.obtenerSuscripcionesDeUsuario(usuarioId);
    }

    public void cancelarSuscripcion(Suscripcion suscripcion) {
        suscripcionService.cancelarSuscripcion(suscripcion);
    }
}