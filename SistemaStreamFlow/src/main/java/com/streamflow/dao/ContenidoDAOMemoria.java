package com.streamflow.dao;

import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContenidoDAOMemoria implements ContenidoDAO {

    private final Map<Integer, Contenido> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Contenido contenido) {
        almacen.put(contenido.getId(), contenido);
    }

    @Override
    public Contenido buscarPorId(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Contenido> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public List<Contenido> listarPorGenero(Genero genero) {
        List<Contenido> resultado = new ArrayList<>();
        for (Contenido contenido : almacen.values()) {
            if (contenido.getGenero() == genero) {
                resultado.add(contenido);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Contenido contenido) {
        almacen.put(contenido.getId(), contenido);
    }

    @Override
    public void eliminar(int id) {
        almacen.remove(id);
    }
}