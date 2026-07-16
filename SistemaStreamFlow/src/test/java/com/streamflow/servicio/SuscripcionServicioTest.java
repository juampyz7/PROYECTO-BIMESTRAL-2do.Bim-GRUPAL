package com.streamflow.servicio;

import com.streamflow.dao.SuscripcionDAO;
import com.streamflow.dao.SuscripcionDAOMemoria;
import com.streamflow.dao.UsuarioDAO;
import com.streamflow.dao.UsuarioDAOMemoria;
import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import com.streamflow.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuscripcionServicioTest {

    private static final String CEDULA_VALIDA = "1103456789";

    private SuscripcionServicio suscripcionServicio;
    private SuscripcionDAO suscripcionDAO;
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void configurar() {
        suscripcionDAO = new SuscripcionDAOMemoria();
        usuarioDAO = new UsuarioDAOMemoria();
        suscripcionServicio = new SuscripcionServicio(suscripcionDAO, usuarioDAO);
        usuarioDAO.guardar(new Usuario(CEDULA_VALIDA, "Juan Perez", "juan@correo.com"));
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
        Suscripcion suscripcion = suscripcionServicio.crearSuscripcion(1, CEDULA_VALIDA, CalidadStreaming.UHD_4K);

        assertEquals(1, suscripcionDAO.listarPorUsuario(CEDULA_VALIDA).size());
        assertEquals(13.99, suscripcion.getCostoMensual());
    }

    @Test
    void crearSuscripcionConUsuarioNoRegistradoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> suscripcionServicio.crearSuscripcion(1, "9999999999", CalidadStreaming.SD));
    }

    @Test
    void cancelarSuscripcionLaMarcaComoInactiva() {
        Suscripcion suscripcion = suscripcionServicio.crearSuscripcion(1, CEDULA_VALIDA, CalidadStreaming.SD);

        suscripcionServicio.cancelarSuscripcion(suscripcion);

        assertFalse(suscripcion.isActiva());
    }

    @Test
    void eliminarSuscripcionLaQuitaDelDAO() {
        suscripcionServicio.crearSuscripcion(1, CEDULA_VALIDA, CalidadStreaming.SD);

        suscripcionServicio.eliminarSuscripcion(1);

        assertTrue(suscripcionDAO.listarPorUsuario(CEDULA_VALIDA).isEmpty());
    }

    @Test
    void eliminarSuscripcionInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> suscripcionServicio.eliminarSuscripcion(999));
    }
}
