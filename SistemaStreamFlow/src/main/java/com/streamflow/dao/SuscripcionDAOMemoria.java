package com.streamflow.dao;

import com.streamflow.modelo.Suscripcion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SuscripcionDAOMemoria implements SuscripcionDAO {

    private final Map<Integer, Suscripcion> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Suscripcion suscripcion) {
        almacen.put(suscripcion.getId(), suscripcion);
    }

    @Override
    public Suscripcion buscarPorId(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Suscripcion> listarPorUsuario(int usuarioId) {
        List<Suscripcion> resultado = new ArrayList<>();
        for (Suscripcion suscripcion : almacen.values()) {
            if (suscripcion.getUsuarioId() == usuarioId) {
                resultado.add(suscripcion);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Suscripcion suscripcion) {
        almacen.put(suscripcion.getId(), suscripcion);
    }
}
