package com.streamflow.dao;

import com.streamflow.modelo.Usuario;

import java.util.List;

public interface UsuarioDAO {

    void guardar(Usuario usuario);

    Usuario buscarPorCedula(String cedula);

    boolean existePorCedula(String cedula);

    List<Usuario> listarTodos();

    void eliminar(String cedula);
}
