package com.streamflow.servicio;

import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;

import java.time.LocalDate;
import java.util.List;

public class SuscripcionServicio {

    private final SuscripcionDAO suscripcionDAO;

    public SuscripcionServicio(SuscripcionDAO suscripcionDAO) {
        this.suscripcionDAO = suscripcionDAO;
    }

    public Suscripcion crearSuscripcion(int id, int usuarioId, CalidadStreaming calidad) {
        double costo = calcularCostoMensual(calidad);
        Suscripcion suscripcion = new Suscripcion(id, usuarioId, calidad, costo, LocalDate.now());
        suscripcionDAO.guardar(suscripcion);
        return suscripcion;
    }

    public double calcularCostoMensual(CalidadStreaming calidad) {
        return calidad.getPrecioBase();
    }

    public double calcularCostoConDescuento(CalidadStreaming calidad, int mesesAntiguedad) {
        double costoBase = calcularCostoMensual(calidad);
        if (mesesAntiguedad >= 12) {
            return costoBase * 0.80;
        }
        if (mesesAntiguedad >= 6) {
            return costoBase * 0.90;
        }
        return costoBase;
    }

    public List<Suscripcion> obtenerSuscripcionesDeUsuario(int usuarioId) {
        return suscripcionDAO.listarPorUsuario(usuarioId);
    }

    public void cancelarSuscripcion(Suscripcion suscripcion) {
        suscripcion.setActiva(false);
        suscripcionDAO.actualizar(suscripcion);
    }
}
