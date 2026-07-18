package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.ContenidoGenerico;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import com.streamflow.modelo.Serie;
import com.streamflow.servicio.RecomendacionServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContenidoDAOSQLiteExtensibilidadTest {

    @TempDir
    Path directorioTemporal;

    private ContenidoDAO contenidoDAO;

    @BeforeEach
    void configurar() {
        String url = "jdbc:sqlite:" + directorioTemporal.resolve("streamflow_ocp_test.db");
        contenidoDAO = new ContenidoDAOSQLite(url);
    }

    @Test
    void unUsuarioPuedeCrearUnTipoDeContenidoQueNoExistiaAntesSinTocarElDAO() {
        Map<String, String> atributos = new LinkedHashMap<>();
        atributos.put("anfitrion", "Daniel Alarcon");
        Contenido podcastInventadoPorElUsuario =
                new ContenidoGenerico(1, "Radio Ambulante", Genero.DRAMA, CalidadStreaming.SD, 40, "Podcast", atributos);

        contenidoDAO.guardar(podcastInventadoPorElUsuario);
        Contenido recuperado = contenidoDAO.buscarPorId(1);

        assertInstanceOf(ContenidoGenerico.class, recuperado);
        assertEquals("Podcast", ((ContenidoGenerico) recuperado).getTipoPersonalizado());
        assertEquals("Daniel Alarcon", ((ContenidoGenerico) recuperado).getAtributosPersonalizados().get("anfitrion"));
    }

    @Test
    void puedeCrearOtroTipoDistintoEnLaMismaEjecucionSinModificarNadaMas() {
        Map<String, String> atributos = new LinkedHashMap<>();
        atributos.put("narrador", "Ana Ruiz");
        Contenido audiolibroInventadoPorElUsuario =
                new ContenidoGenerico(2, "Cien Anios de Soledad", Genero.DRAMA, CalidadStreaming.HD, 620, "AudioLibro", atributos);

        contenidoDAO.guardar(audiolibroInventadoPorElUsuario);
        Contenido recuperado = contenidoDAO.buscarPorId(2);

        assertEquals("AudioLibro", ((ContenidoGenerico) recuperado).getTipoPersonalizado());
    }

    @Test
    void elServicioDeRecomendacionFuncionaConElTipoInventadoSinCambios() {
        contenidoDAO.guardar(new Pelicula(1, "Matrix", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 136, "Wachowski"));
        contenidoDAO.guardar(new Serie(2, "Dark", Genero.CIENCIA_FICCION, CalidadStreaming.HD, 60, 3, 8));
        contenidoDAO.guardar(new Documental(3, "Cosmos", Genero.CIENCIA_FICCION, CalidadStreaming.SD, 45, "Universo"));

        Map<String, String> atributos = new LinkedHashMap<>();
        atributos.put("anfitrion", "Ana Ruiz");
        contenidoDAO.guardar(new ContenidoGenerico(4, "Ciencia Ficcion Hoy", Genero.CIENCIA_FICCION,
                CalidadStreaming.SD, 40, "Podcast", atributos));

        RecomendacionServicio recomendacionServicio = new RecomendacionServicio(contenidoDAO);
        List<Contenido> recomendados = recomendacionServicio.recomendarPorGenero(Genero.CIENCIA_FICCION);

        assertEquals(4, recomendados.size());
        assertTrue(recomendados.stream().anyMatch(c -> c instanceof ContenidoGenerico));
    }
}
