package com.streamflow.servicio;

import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.dao.SuscripcionDAOMemoria;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SuscripcionServicioTest {

    private SuscripcionServicio suscripcionServicio;
    private SuscripcionDAO suscripcionDAO;

    @BeforeEach
    void configurar() {
        suscripcionDAO = new SuscripcionDAOMemoria();
        suscripcionServicio = new SuscripcionServicio(suscripcionDAO);
    }

    @Test
    void calcularCostoMensualSegunCalidad() {
        assertEquals(4.99, suscripcionServicio.calcularCostoMensual(CalidadStreaming.SD));
        assertEquals(8.99, suscripcionServicio.calcularCostoMensual(CalidadStreaming.HD));
        assertEquals(13.99, suscripcionServicio.calcularCostoMensual(CalidadStreaming.UHD_4K));
    }

    @Test
    void calcularCostoConDescuentoPorAntiguedad() {
        double costoSinDescuento = suscripcionServicio.calcularCostoConDescuento(CalidadStreaming.HD, 2);
        double costoDescuentoMedio = suscripcionServicio.calcularCostoConDescuento(CalidadStreaming.HD, 6);
        double costoDescuentoAlto = suscripcionServicio.calcularCostoConDescuento(CalidadStreaming.HD, 12);

        assertEquals(8.99, costoSinDescuento, 0.001);
        assertEquals(8.09, costoDescuentoMedio, 0.01);
        assertEquals(7.19, costoDescuentoAlto, 0.01);
    }

    @Test
    void crearSuscripcionLaPersisteEnElDAO() {
        Suscripcion suscripcion = suscripcionServicio.crearSuscripcion(1, 100, CalidadStreaming.UHD_4K);

        assertEquals(1, suscripcionDAO.listarPorUsuario(100).size());
        assertEquals(13.99, suscripcion.getCostoMensual());
    }

    @Test
    void cancelarSuscripcionLaMarcaComoInactiva() {
        Suscripcion suscripcion = suscripcionServicio.crearSuscripcion(1, 100, CalidadStreaming.SD);

        suscripcionServicio.cancelarSuscripcion(suscripcion);

        assertFalse(suscripcion.isActiva());
    }
}
