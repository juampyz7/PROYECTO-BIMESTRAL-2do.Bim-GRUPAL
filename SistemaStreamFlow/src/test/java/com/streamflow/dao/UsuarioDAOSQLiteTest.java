package com.streamflow.dao;

import com.streamflow.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioDAOSQLiteTest {

    @TempDir
    Path directorioTemporal;

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void configurar() {
        String url = "jdbc:sqlite:" + directorioTemporal.resolve("streamflow_test.db");
        usuarioDAO = new UsuarioDAOSQLite(url);
    }

    @Test
    void guardarYBuscarPorCedulaConservaLosDatos() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        Usuario recuperado = usuarioDAO.buscarPorCedula("1103456789");

        assertEquals("Juan Perez", recuperado.getNombres());
        assertEquals("juan@correo.com", recuperado.getEmail());
    }

    @Test
    void noPermiteRegistrarLaMismaCedulaDosVeces() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        assertThrows(IllegalStateException.class,
                () -> usuarioDAO.guardar(new Usuario("1103456789", "Otro Nombre", "otro@correo.com")));
    }

    @Test
    void eliminarQuitaElRegistroDeLaBaseDeDatos() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        usuarioDAO.eliminar("1103456789");

        assertNull(usuarioDAO.buscarPorCedula("1103456789"));
    }
}
