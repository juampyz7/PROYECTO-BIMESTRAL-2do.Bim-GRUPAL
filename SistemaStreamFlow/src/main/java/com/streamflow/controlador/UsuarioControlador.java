package com.streamflow.controlador;

import com.streamflow.dao.UsuarioDAO;
import com.streamflow.modelo.Usuario;

import java.util.List;

public class UsuarioControlador {

    private final UsuarioDAO usuarioDAO;

    public UsuarioControlador(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario registrarUsuario(String cedula, String nombres, String email) {
        Usuario usuario = new Usuario(cedula, nombres, email);
        usuarioDAO.guardar(usuario);
        return usuario;
    }

    public Usuario buscarUsuario(String cedula) {
        return usuarioDAO.buscarPorCedula(cedula);
    }

    public boolean existeUsuario(String cedula) {
        return usuarioDAO.existePorCedula(cedula);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    public void eliminarUsuario(String cedula) {
        usuarioDAO.eliminar(cedula);
    }
}

