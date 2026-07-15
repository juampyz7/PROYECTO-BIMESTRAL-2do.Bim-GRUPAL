package com.streamflow.servicio;

import com.streamflow.dao.ContenidoDAO;
import com.streamflow.dao.ContenidoDAOMemoria;
import com.streamflow.model.CalidadStreaming;
import com.streamflow.model.Contenido;
import com.streamflow.model.Genero;
import com.streamflow.model.Pelicula;
import com.streamflow.model.Serie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecomendacionServicioTest {

    private RecomendacionServicio recomendacionServicio;
    private ContenidoDAO contenidoDAO;

    @BeforeEach
    void configurar() {
        contenidoDAO = new ContenidoDAOMemoria();
        recomendacionService = new RecomendacionService(contenidoDAO);
    }

    @Test
    void recomendarPorGeneroOrdenaDeMayorAMenorFactor() {
        contenidoDAO.guardar(new Pelicula(1, "Corta", Genero.DRAMA, CalidadStreaming.HD, 90, "Director A"));
        contenidoDAO.guardar(new Serie(2, "Serie larga", Genero.DRAMA, CalidadStreaming.HD, 45, 5, 10));
        contenidoDAO.guardar(new Pelicula(3, "Comedia", Genero.COMEDIA, CalidadStreaming.HD, 100, "Director B"));

        List<Contenido> recomendados = recomendacionService.recomendarPorGenero(Genero.DRAMA);

        assertEquals(2, recomendados.size());
        assertEquals("Serie larga", recomendados.get(0).getTitulo());
    }

    @Test
    void obtenerResumenReproduccionIncluyeReproduccionYDetalles() {
        Contenido pelicula = new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski");

        String resumen = recomendacionService.obtenerResumenReproduccion(pelicula);

        assertTrue(resumen.contains("Reproduciendo pelicula"));
        assertTrue(resumen.contains("Matrix"));
    }
}
