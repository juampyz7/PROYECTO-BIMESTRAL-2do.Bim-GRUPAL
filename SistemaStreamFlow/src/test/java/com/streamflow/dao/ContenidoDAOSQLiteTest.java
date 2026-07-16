package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import com.streamflow.modelo.Serie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContenidoDAOSQLiteTest {

    @TempDir
    Path directorioTemporal;

    private ContenidoDAO contenidoDAO;

    @BeforeEach
    void configurar() {
        String url = "jdbc:sqlite:" + directorioTemporal.resolve("streamflow_test.db");
        contenidoDAO = new ContenidoDAOSQLite(url);
    }

    @Test
    void guardarYRecuperarPeliculaConservaLosDatos() {
        Contenido pelicula = new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski");
        contenidoDAO.guardar(pelicula);

        Contenido recuperado = contenidoDAO.buscarPorId(1);

        assertNotNull(recuperado);
        assertInstanceOf(Pelicula.class, recuperado);
        assertEquals("Matrix", recuperado.getTitulo());
        assertEquals("Wachowski", ((Pelicula) recuperado).getDirector());
    }

    @Test
    void guardarYRecuperarSerieConservaTemporadasYEpisodios() {
        Contenido serie = new Serie(2, "Dark", Genero.DRAMA, CalidadStreaming.HD, 60, 3, 8);
        contenidoDAO.guardar(serie);

        Contenido recuperado = contenidoDAO.buscarPorId(2);

        assertInstanceOf(Serie.class, recuperado);
        assertEquals(3, ((Serie) recuperado).getTemporadas());
        assertEquals(8, ((Serie) recuperado).getEpisodiosPorTemporada());
    }

    @Test
    void guardarYRecuperarDocumentalConservaElTema() {
        Contenido documental = new Documental(3, "Cosmos", Genero.DOCUMENTAL, CalidadStreaming.SD, 45, "Universo");
        contenidoDAO.guardar(documental);

        Contenido recuperado = contenidoDAO.buscarPorId(3);

        assertInstanceOf(Documental.class, recuperado);
        assertEquals("Universo", ((Documental) recuperado).getTema());
    }

    @Test
    void listarPorGeneroFiltraCorrectamenteEntreTipos() {
        contenidoDAO.guardar(new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski"));
        contenidoDAO.guardar(new Serie(2, "Dark", Genero.DRAMA, CalidadStreaming.HD, 60, 3, 8));

        List<Contenido> resultado = contenidoDAO.listarPorGenero(Genero.CIENCIA_FICCION);

        assertEquals(1, resultado.size());
        assertEquals("Matrix", resultado.get(0).getTitulo());
    }

    @Test
    void actualizarModificaLosDatosPersistidos() {
        Contenido pelicula = new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski");
        contenidoDAO.guardar(pelicula);

        pelicula.setTitulo("Matrix Reloaded");
        contenidoDAO.actualizar(pelicula);

        Contenido recuperado = contenidoDAO.buscarPorId(1);
        assertEquals("Matrix Reloaded", recuperado.getTitulo());
    }

    @Test
    void eliminarQuitaElRegistroDeLaBaseDeDatos() {
        contenidoDAO.guardar(new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski"));

        contenidoDAO.eliminar(1);

        assertNull(contenidoDAO.buscarPorId(1));
        assertTrue(contenidoDAO.listarTodos().isEmpty());
    }
}
