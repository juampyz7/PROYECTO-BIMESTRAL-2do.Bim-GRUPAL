package com.streamflow.controlador;

import com.streamflow.dao.ContenidoDAO;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

import java.util.List;

public class ContenidoControlador {

    private final ContenidoDAO contenidoDAO;

    public ContenidoControlador(ContenidoDAO contenidoDAO) {
        this.contenidoDAO = contenidoDAO;
    }

    public void agregarContenido(Contenido contenido) {
        contenidoDAO.guardar(contenido);
    }

    public Contenido buscarContenido(int id) {
        return contenidoDAO.buscarPorId(id);
    }

    public List<Contenido> listarContenido() {
        return contenidoDAO.listarTodos();
    }

    public List<Contenido> listarContenidoPorGenero(Genero genero) {
        return contenidoDAO.listarPorGenero(genero);
    }

    public void eliminarContenido(int id) {
        contenidoDAO.eliminar(id);
    }
}

