package com.streamflow.dao;

import com.streamflow.modelo.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private final Map<Integer, Usuario> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        almacen.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario buscarPorId(int id) {
        return almacen.get(id);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public void eliminar(int id) {
        almacen.remove(id);
    }
}