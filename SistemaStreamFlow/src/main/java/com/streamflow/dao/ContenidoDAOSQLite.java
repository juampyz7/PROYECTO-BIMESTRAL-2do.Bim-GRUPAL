package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Documental;
import com.streamflow.modelo.Genero;
import com.streamflow.modelo.Pelicula;
import com.streamflow.modelo.Serie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContenidoDAOSQLite implements ContenidoDAO {

    private final String urlConexion;

    public ContenidoDAOSQLite(String urlConexion) {
        this.urlConexion = urlConexion;
        crearTablaSiNoExiste();
    }

    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(urlConexion);
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS contenido ("
                + "id INTEGER PRIMARY KEY,"
                + "titulo TEXT NOT NULL,"
                + "genero TEXT NOT NULL,"
                + "calidad TEXT NOT NULL,"
                + "duracion_minutos INTEGER NOT NULL,"
                + "tipo TEXT NOT NULL,"
                + "director TEXT,"
                + "temporadas INTEGER,"
                + "episodios_por_temporada INTEGER,"
                + "tema TEXT)";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla contenido", e);
        }
    }

    @Override
    public void guardar(Contenido contenido) {
        String sql = "INSERT INTO contenido "
                + "(id, titulo, genero, calidad, duracion_minutos, tipo, director, temporadas, episodios_por_temporada, tema) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            asignarParametros(statement, contenido);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar contenido", e);
        }
    }

    @Override
    public Contenido buscarPorId(int id) {
        String sql = "SELECT * FROM contenido WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return construirContenido(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar contenido por id", e);
        }
    }

    @Override
    public List<Contenido> listarTodos() {
        List<Contenido> resultado = new ArrayList<>();
        String sql = "SELECT * FROM contenido";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                resultado.add(construirContenido(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contenido", e);
        }
        return resultado;
    }

    @Override
    public List<Contenido> listarPorGenero(Genero genero) {
        List<Contenido> resultado = new ArrayList<>();
        String sql = "SELECT * FROM contenido WHERE genero = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, genero.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    resultado.add(construirContenido(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar contenido por genero", e);
        }
        return resultado;
    }

    @Override
    public void actualizar(Contenido contenido) {
        String sql = "UPDATE contenido SET titulo = ?, genero = ?, calidad = ?, duracion_minutos = ?, "
                + "tipo = ?, director = ?, temporadas = ?, episodios_por_temporada = ?, tema = ? WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, contenido.getTitulo());
            statement.setString(2, contenido.getGenero().name());
            statement.setString(3, contenido.getCalidad().name());
            statement.setInt(4, contenido.getDuracionMinutos());
            statement.setString(5, obtenerTipo(contenido));
            asignarCamposEspecificos(statement, contenido, 6);
            statement.setInt(10, contenido.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar contenido", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM contenido WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar contenido", e);
        }
    }

    private void asignarParametros(PreparedStatement statement, Contenido contenido) throws SQLException {
        statement.setInt(1, contenido.getId());
        statement.setString(2, contenido.getTitulo());
        statement.setString(3, contenido.getGenero().name());
        statement.setString(4, contenido.getCalidad().name());
        statement.setInt(5, contenido.getDuracionMinutos());
        statement.setString(6, obtenerTipo(contenido));
        asignarCamposEspecificos(statement, contenido, 7);
    }

    private void asignarCamposEspecificos(PreparedStatement statement, Contenido contenido, int indiceInicial) throws SQLException {
        if (contenido instanceof Pelicula pelicula) {
            statement.setString(indiceInicial, pelicula.getDirector());
            statement.setNull(indiceInicial + 1, java.sql.Types.INTEGER);
            statement.setNull(indiceInicial + 2, java.sql.Types.INTEGER);
            statement.setNull(indiceInicial + 3, java.sql.Types.VARCHAR);
        } else if (contenido instanceof Serie serie) {
            statement.setNull(indiceInicial, java.sql.Types.VARCHAR);
            statement.setInt(indiceInicial + 1, serie.getTemporadas());
            statement.setInt(indiceInicial + 2, serie.getEpisodiosPorTemporada());
            statement.setNull(indiceInicial + 3, java.sql.Types.VARCHAR);
        } else if (contenido instanceof Documental documental) {
            statement.setNull(indiceInicial, java.sql.Types.VARCHAR);
            statement.setNull(indiceInicial + 1, java.sql.Types.INTEGER);
            statement.setNull(indiceInicial + 2, java.sql.Types.INTEGER);
            statement.setString(indiceInicial + 3, documental.getTema());
        }
    }

    private String obtenerTipo(Contenido contenido) {
        if (contenido instanceof Pelicula) {
            return "PELICULA";
        }
        if (contenido instanceof Serie) {
            return "SERIE";
        }
        if (contenido instanceof Documental) {
            return "DOCUMENTAL";
        }
        throw new IllegalArgumentException("Tipo de contenido no soportado");
    }

    private Contenido construirContenido(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String titulo = resultSet.getString("titulo");
        Genero genero = Genero.valueOf(resultSet.getString("genero"));
        CalidadStreaming calidad = CalidadStreaming.valueOf(resultSet.getString("calidad"));
        int duracion = resultSet.getInt("duracion_minutos");
        String tipo = resultSet.getString("tipo");

        return switch (tipo) {
            case "PELICULA" -> new Pelicula(id, titulo, genero, calidad, duracion, resultSet.getString("director"));
            case "SERIE" -> new Serie(id, titulo, genero, calidad, duracion,
                    resultSet.getInt("temporadas"), resultSet.getInt("episodios_por_temporada"));
            case "DOCUMENTAL" -> new Documental(id, titulo, genero, calidad, duracion, resultSet.getString("tema"));
            default -> throw new IllegalStateException("Tipo de contenido desconocido: " + tipo);
        };
    }
}
