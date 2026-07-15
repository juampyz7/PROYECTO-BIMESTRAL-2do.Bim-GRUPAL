package com.streamflow.dao;

import com.streamflow.modelo.Usuario;

import java.util.List;

public interface UsuarioDAO {

    void guardar(Usuario usuario);

    Usuario buscarPorId(int id);

    List<Usuario> listarTodos();

    void eliminar(int id);
}
