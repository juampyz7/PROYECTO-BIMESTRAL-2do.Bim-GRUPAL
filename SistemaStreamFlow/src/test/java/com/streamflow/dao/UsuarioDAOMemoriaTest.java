package com.streamflow.dao;

import com.streamflow.modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioDAOMemoriaTest {

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void configurar() {
        usuarioDAO = new UsuarioDAOMemoria();
    }

    @Test
    void guardarYBuscarPorCedulaFuncionaCorrectamente() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        Usuario encontrado = usuarioDAO.buscarPorCedula("1103456789");

        assertEquals("Juan Perez", encontrado.getNombres());
    }

    @Test
    void noPermiteRegistrarLaMismaCedulaDosVeces() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        assertThrows(IllegalStateException.class,
                () -> usuarioDAO.guardar(new Usuario("1103456789", "Otro Nombre", "otro@correo.com")));
    }

    @Test
    void existePorCedulaDetectaCorrectamente() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        assertTrue(usuarioDAO.existePorCedula("1103456789"));
        assertEquals(false, usuarioDAO.existePorCedula("9999999999"));
    }

    @Test
    void eliminarQuitaElUsuarioDelAlmacen() {
        usuarioDAO.guardar(new Usuario("1103456789", "Juan Perez", "juan@correo.com"));

        usuarioDAO.eliminar("1103456789");

        assertNull(usuarioDAO.buscarPorCedula("1103456789"));
    }
}
