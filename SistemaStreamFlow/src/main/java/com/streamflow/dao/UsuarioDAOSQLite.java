package com.streamflow.dao;

import com.streamflow.modelo.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOSQLite implements UsuarioDAO {

    private final String urlConexion;

    public UsuarioDAOSQLite(String urlConexion) {
        this.urlConexion = urlConexion;
        crearTablaSiNoExiste();
    }

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(urlConexion);
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + "cedula TEXT PRIMARY KEY,"
                + "nombres TEXT NOT NULL,"
                + "email TEXT)";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla usuario", e);
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario (cedula, nombres, email) VALUES (?, ?, ?)";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, usuario.getCedula());
            statement.setString(2, usuario.getNombres());
            statement.setString(3, usuario.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toUpperCase().contains("UNIQUE")) {
                throw new IllegalStateException("Ya existe un usuario registrado con la cedula " + usuario.getCedula(), e);
            }
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    @Override
    public Usuario buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM usuario WHERE cedula = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cedula);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return construirUsuario(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por cedula", e);
        }
    }

    @Override
    public boolean existePorCedula(String cedula) {
        String sql = "SELECT 1 FROM usuario WHERE cedula = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cedula);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de usuario", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> resultado = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                resultado.add(construirUsuario(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return resultado;
    }

    @Override
    public void eliminar(String cedula) {
        String sql = "DELETE FROM usuario WHERE cedula = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cedula);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    private Usuario construirUsuario(ResultSet resultSet) throws SQLException {
        return new Usuario(resultSet.getString("cedula"), resultSet.getString("nombres"), resultSet.getString("email"));
    }
}
