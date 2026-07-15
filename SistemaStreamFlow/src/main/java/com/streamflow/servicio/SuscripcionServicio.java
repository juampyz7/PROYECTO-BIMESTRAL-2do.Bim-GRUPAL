package com.streamflow.servicio;

import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.dao.UsuarioDAO;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;

import java.time.LocalDate;
import java.util.List;

public class SuscripcionServicio {

    private final SuscripcionDAO suscripcionDAO;
    private final UsuarioDAO usuarioDAO;

    public SuscripcionServicio(SuscripcionDAO suscripcionDAO, UsuarioDAO usuarioDAO) {
        this.suscripcionDAO = suscripcionDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public Suscripcion crearSuscripcion(int id, String cedulaUsuario, CalidadStreaming calidad) {
        if (!usuarioDAO.existePorCedula(cedulaUsuario)) {
            throw new IllegalArgumentException("No existe un usuario registrado con la cedula " + cedulaUsuario);
        }
        double costo = calcularCostoMensual(calidad);
        Suscripcion suscripcion = new Suscripcion(id, cedulaUsuario, calidad, costo, LocalDate.now());
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

    public List<Suscripcion> obtenerSuscripcionesDeUsuario(String cedulaUsuario) {
        return suscripcionDAO.listarPorUsuario(cedulaUsuario);
    }

    public void cancelarSuscripcion(Suscripcion suscripcion) {
        suscripcion.setActiva(false);
        suscripcionDAO.actualizar(suscripcion);
    }

    public void eliminarSuscripcion(int id) {
        Suscripcion suscripcion = suscripcionDAO.buscarPorId(id);
        if (suscripcion == null) {
            throw new IllegalArgumentException("No existe una suscripcion con el id " + id);
        }
        suscripcionDAO.eliminar(id);
    }
}
