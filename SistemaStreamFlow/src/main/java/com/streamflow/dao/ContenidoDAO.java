package com.streamflow.dao;

import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

import java.util.List;

public interface ContenidoDAO {

    void guardar(Contenido contenido);

    Contenido buscarPorId(int id);

    List<Contenido> listarTodos();

    List<Contenido> listarPorGenero(Genero genero);

    void actualizar(Contenido contenido);

    void eliminar(int id);
}

