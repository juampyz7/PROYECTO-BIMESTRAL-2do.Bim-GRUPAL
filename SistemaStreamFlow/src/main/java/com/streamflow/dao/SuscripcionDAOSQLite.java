package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Suscripcion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuscripcionDAOSQLite implements SuscripcionDAO {

    private final String urlConexion;

    public SuscripcionDAOSQLite(String urlConexion) {
        this.urlConexion = urlConexion;
        crearTablaSiNoExiste();
    }

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(urlConexion);
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS suscripcion ("
                + "id INTEGER PRIMARY KEY,"
                + "cedula_usuario TEXT NOT NULL,"
                + "calidad TEXT NOT NULL,"
                + "costo_mensual REAL NOT NULL,"
                + "fecha_inicio TEXT NOT NULL,"
                + "activa INTEGER NOT NULL,"
                + "FOREIGN KEY (cedula_usuario) REFERENCES usuario(cedula))";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla suscripcion", e);
        }
    }

    @Override
    public void guardar(Suscripcion suscripcion) {
        String sql = "INSERT INTO suscripcion (id, cedula_usuario, calidad, costo_mensual, fecha_inicio, activa) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            asignarParametros(statement, suscripcion);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar suscripcion", e);
        }
    }

    @Override
    public Suscripcion buscarPorId(int id) {
        String sql = "SELECT * FROM suscripcion WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return construirSuscripcion(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar suscripcion por id", e);
        }
    }

    @Override
    public List<Suscripcion> listarPorUsuario(String cedulaUsuario) {
        List<Suscripcion> resultado = new ArrayList<>();
        String sql = "SELECT * FROM suscripcion WHERE cedula_usuario = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, cedulaUsuario);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    resultado.add(construirSuscripcion(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar suscripciones por usuario", e);
        }
        return resultado;
    }

    @Override
    public List<Suscripcion> listarTodas() {
        List<Suscripcion> resultado = new ArrayList<>();
        String sql = "SELECT * FROM suscripcion";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                resultado.add(construirSuscripcion(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las suscripciones", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Suscripcion suscripcion) {
        String sql = "UPDATE suscripcion SET cedula_usuario = ?, calidad = ?, costo_mensual = ?, "
                + "fecha_inicio = ?, activa = ? WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, suscripcion.getCedulaUsuario());
            statement.setString(2, suscripcion.getCalidad().name());
            statement.setDouble(3, suscripcion.getCostoMensual());
            statement.setString(4, suscripcion.getFechaInicio().toString());
            statement.setInt(5, suscripcion.isActiva() ? 1 : 0);
            statement.setInt(6, suscripcion.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar suscripcion", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM suscripcion WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar suscripcion", e);
        }
    }

    private void asignarParametros(PreparedStatement statement, Suscripcion suscripcion) throws SQLException {
        statement.setInt(1, suscripcion.getId());
        statement.setString(2, suscripcion.getCedulaUsuario());
        statement.setString(3, suscripcion.getCalidad().name());
        statement.setDouble(4, suscripcion.getCostoMensual());
        statement.setString(5, suscripcion.getFechaInicio().toString());
        statement.setInt(6, suscripcion.isActiva() ? 1 : 0);
    }

    private Suscripcion construirSuscripcion(ResultSet resultSet) throws SQLException {
        Suscripcion suscripcion = new Suscripcion(
                resultSet.getInt("id"),
                resultSet.getString("cedula_usuario"),
                CalidadStreaming.valueOf(resultSet.getString("calidad")),
                resultSet.getDouble("costo_mensual"),
                LocalDate.parse(resultSet.getString("fecha_inicio"))
        );
        suscripcion.setActiva(resultSet.getInt("activa") == 1);
        return suscripcion;
    }

    @Override
    public List<Suscripcion> listarPorUsuario(int usuarioId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
