package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContenidoDAOMemoriaTest {

    private ContenidoDAO contenidoDAO;

    @BeforeEach
    void configurar() {
        contenidoDAO = new ContenidoDAOMemoria();
    }

    @Test
    void guardarYBuscarPorIdRetornaElContenidoCorrecto() {
        Contenido pelicula = new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski");
        contenidoDAO.guardar(pelicula);

        Contenido encontrado = contenidoDAO.buscarPorId(1);

        assertNotNull(encontrado);
        assertEquals("Matrix", encontrado.getTitulo());
    }

    @Test
    void listarPorGeneroFiltraCorrectamente() {
        contenidoDAO.guardar(new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski"));
        contenidoDAO.guardar(new Pelicula(2, "Titanic", Genero.DRAMA, CalidadStreaming.HD, 195, "James Cameron"));

        List<Contenido> resultado = contenidoDAO.listarPorGenero(Genero.CIENCIA_FICCION);

        assertEquals(1, resultado.size());
        assertEquals("Matrix", resultado.get(0).getTitulo());
    }

    @Test
    void eliminarQuitaElContenidoDelAlmacen() {
        contenidoDAO.guardar(new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski"));
        contenidoDAO.eliminar(1);

        assertNull(contenidoDAO.buscarPorId(1));
        assertTrue(contenidoDAO.listarTodos().isEmpty());
    }
}

