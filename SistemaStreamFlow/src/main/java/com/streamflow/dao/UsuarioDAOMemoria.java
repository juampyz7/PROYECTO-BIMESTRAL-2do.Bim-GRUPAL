package com.streamflow.dao;

import com.streamflow.modelo.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private final Map<String, Usuario> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        if (almacen.containsKey(usuario.getCedula())) {
            throw new IllegalStateException("Ya existe un usuario registrado con la cedula " + usuario.getCedula());
        }
        almacen.put(usuario.getCedula(), usuario);
    }

    @Override
    public Usuario buscarPorCedula(String cedula) {
        return almacen.get(cedula);
    }

    @Override
    public boolean existePorCedula(String cedula) {
        return almacen.containsKey(cedula);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public void eliminar(String cedula) {
        almacen.remove(cedula);
    }
}
