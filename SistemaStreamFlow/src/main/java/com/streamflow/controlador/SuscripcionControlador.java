package com.streamflow.controlador;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.servicio.SuscripcionServicio;

import java.util.List;

public class SuscripcionControlador {

    private final SuscripcionServicio suscripcionServicio;

    public SuscripcionControlador(SuscripcionServicio suscripcionServicio) {
        this.suscripcionServicio = suscripcionServicio;
    }

    public Suscripcion registrarSuscripcion(int id, String cedulaUsuario, CalidadStreaming calidad) {
        return suscripcionServicio.crearSuscripcion(id, cedulaUsuario, calidad);
    }

    public List<Suscripcion> obtenerSuscripciones(String cedulaUsuario) {
        return suscripcionServicio.obtenerSuscripcionesDeUsuario(cedulaUsuario);
    }

    public void cancelarSuscripcion(Suscripcion suscripcion) {
        suscripcionServicio.cancelarSuscripcion(suscripcion);
    }

    public void eliminarSuscripcion(int id) {
        suscripcionServicio.eliminarSuscripcion(id);
    }
}
