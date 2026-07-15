package com.streamflow.modelo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContenidoTest {

    @Test
    void reproducirEsPolimorficoParaCadaTipoDeContenido() {
        List<Contenido> contenidos = new ArrayList<>();
        contenidos.add(new Pelicula(1, "Inception", Genero.CIENCIA_FICCION, CalidadStreaming.UHD_4K, 148, "Christopher Nolan"));
        contenidos.add(new Serie(2, "Dark", Genero.DRAMA, CalidadStreaming.HD, 60, 3, 8));
        contenidos.add(new Documental(3, "Cosmos", Genero.DOCUMENTAL, CalidadStreaming.SD, 45, "Universo"));

        for (Contenido contenido : contenidos) {
            String resultado = contenido.reproducir();
            assertTrue(resultado.contains(contenido.getTitulo()));
        }
    }

    @Test
    void peliculaLargaTieneMayorFactorDeRecomendacion() {
        Pelicula peliculaLarga = new Pelicula(1, "Larga", Genero.ACCION, CalidadStreaming.HD, 180, "Director X");
        Pelicula peliculaCorta = new Pelicula(2, "Corta", Genero.ACCION, CalidadStreaming.HD, 90, "Director Y");

        assertTrue(peliculaLarga.calcularFactorRecomendacion() > peliculaCorta.calcularFactorRecomendacion());
    }

    @Test
    void serieConMasTemporadasTieneMayorFactor() {
        Serie serieCorta = new Serie(1, "Serie A", Genero.DRAMA, CalidadStreaming.HD, 45, 1, 10);
        Serie serieLarga = new Serie(2, "Serie B", Genero.DRAMA, CalidadStreaming.HD, 45, 5, 10);

        assertEquals(0.5, serieCorta.calcularFactorRecomendacion());
        assertEquals(2.5, serieLarga.calcularFactorRecomendacion());
    }
}
