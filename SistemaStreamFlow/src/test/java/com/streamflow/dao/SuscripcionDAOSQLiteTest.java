package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuscripcionDAOSQLiteTest {

    private static final String CEDULA_A = "1103456789";
    private static final String CEDULA_B = "1709876543";

    @TempDir
    Path directorioTemporal;

    private SuscripcionDAO suscripcionDAO;

    @BeforeEach
    void configurar() {
        String url = "jdbc:sqlite:" + directorioTemporal.resolve("streamflow_test.db");
        suscripcionDAO = new SuscripcionDAOSQLite(url);
    }

    @Test
    void guardarYBuscarPorIdConservaLosDatos() {
        Suscripcion suscripcion = new Suscripcion(1, CEDULA_A, CalidadStreaming.UHD_4K, 13.99, LocalDate.of(2026, 1, 1));
        suscripcionDAO.guardar(suscripcion);

        Suscripcion recuperada = suscripcionDAO.buscarPorId(1);

        assertEquals(CEDULA_A, recuperada.getCedulaUsuario());
        assertEquals(CalidadStreaming.UHD_4K, recuperada.getCalidad());
        assertEquals(13.99, recuperada.getCostoMensual());
        assertTrue(recuperada.isActiva());
    }

    @Test
    void listarPorUsuarioFiltraCorrectamente() {
        suscripcionDAO.guardar(new Suscripcion(1, CEDULA_A, CalidadStreaming.SD, 4.99, LocalDate.now()));
        suscripcionDAO.guardar(new Suscripcion(2, CEDULA_B, CalidadStreaming.HD, 8.99, LocalDate.now()));

        List<Suscripcion> resultado = suscripcionDAO.listarPorUsuario(CEDULA_A);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
    }

    @Test
    void actualizarMarcaLaSuscripcionComoInactiva() {
        Suscripcion suscripcion = new Suscripcion(1, CEDULA_A, CalidadStreaming.SD, 4.99, LocalDate.now());
        suscripcionDAO.guardar(suscripcion);

        suscripcion.setActiva(false);
        suscripcionDAO.actualizar(suscripcion);

        Suscripcion recuperada = suscripcionDAO.buscarPorId(1);
        assertFalse(recuperada.isActiva());
    }

    @Test
    void eliminarQuitaElRegistroDeLaBaseDeDatos() {
        suscripcionDAO.guardar(new Suscripcion(1, CEDULA_A, CalidadStreaming.SD, 4.99, LocalDate.now()));

        suscripcionDAO.eliminar(1);

        assertNull(suscripcionDAO.buscarPorId(1));
        assertTrue(suscripcionDAO.listarTodas().isEmpty());
    }
}

