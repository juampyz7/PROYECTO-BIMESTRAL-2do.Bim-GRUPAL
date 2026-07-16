package com.streamflow.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {

    @Test
    void creaUsuarioValidoCorrectamente() {
        Usuario usuario = new Usuario("1103456789", "Juan Perez", "juan@correo.com");

        assertEquals("1103456789", usuario.getCedula());
        assertEquals("Juan Perez", usuario.getNombres());
    }

    @Test
    void rechazaCedulaConMenosDeDiezDigitos() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario("12345", "Juan Perez", "juan@correo.com"));
    }

    @Test
    void rechazaCedulaConLetras() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario("110345678A", "Juan Perez", "juan@correo.com"));
    }

    @Test
    void rechazaNombresVacios() {
        assertThrows(IllegalArgumentException.class, () -> new Usuario("1103456789", "  ", "juan@correo.com"));
    }
}
