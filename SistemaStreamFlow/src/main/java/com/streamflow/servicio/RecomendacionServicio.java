package com.streamflow.servicio;

import com.streamflow.dao.ContenidoDAO;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

import java.util.Comparator;
import java.util.List;

public class RecomendacionServicio {

    private final ContenidoDAO contenidoDAO;

    public RecomendacionServicio(ContenidoDAO contenidoDAO) {
        this.contenidoDAO = contenidoDAO;
    }

    public List<Contenido> recomendarPorGenero(Genero genero) {
        List<Contenido> candidatos = contenidoDAO.listarPorGenero(genero);
        candidatos.sort(Comparator.comparingDouble(Contenido::calcularFactorRecomendacion).reversed());
        return candidatos;
    }

    public String obtenerResumenReproduccion(Contenido contenido) {
        return contenido.reproducir() + " | " + contenido.obtenerDetalles();
    }
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.streamflow.servicio;

/**
 *
 * @author Usuario iTC
 */
public class RecomendacionServicio {
    
}
