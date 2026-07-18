package com.streamflow.dao;

import com.streamflow.modelo.CalidadStreaming;
import com.streamflow.modelo.Contenido;
import com.streamflow.modelo.Genero;

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
    private final List<ContenidoTipoMapeador> mapeadores;

    public ContenidoDAOSQLite(String urlConexion) {
        this(urlConexion, List.of(new PeliculaMapeador(), new SerieMapeador(), new DocumentalMapeador(), new ContenidoGenericoMapeador()));
    }

    public ContenidoDAOSQLite(String urlConexion, List<ContenidoTipoMapeador> mapeadores) {
        this.urlConexion = urlConexion;
        this.mapeadores = mapeadores;
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
                + "atributos_extra TEXT)";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la tabla contenido", e);
        }
    }

    @Override
    public void guardar(Contenido contenido) {
        String sql = "INSERT INTO contenido (id, titulo, genero, calidad, duracion_minutos, tipo, atributos_extra) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                + "tipo = ?, atributos_extra = ? WHERE id = ?";
        try (Connection conexion = obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            ContenidoTipoMapeador mapeador = obtenerMapeador(contenido);
            statement.setString(1, contenido.getTitulo());
            statement.setString(2, contenido.getGenero().name());
            statement.setString(3, contenido.getCalidad().name());
            statement.setInt(4, contenido.getDuracionMinutos());
            statement.setString(5, mapeador.obtenerTipo());
            statement.setString(6, mapeador.serializarAtributos(contenido));
            statement.setInt(7, contenido.getId());
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
        ContenidoTipoMapeador mapeador = obtenerMapeador(contenido);
        statement.setInt(1, contenido.getId());
        statement.setString(2, contenido.getTitulo());
        statement.setString(3, contenido.getGenero().name());
        statement.setString(4, contenido.getCalidad().name());
        statement.setInt(5, contenido.getDuracionMinutos());
        statement.setString(6, mapeador.obtenerTipo());
        statement.setString(7, mapeador.serializarAtributos(contenido));
    }

    private ContenidoTipoMapeador obtenerMapeador(Contenido contenido) {
        for (ContenidoTipoMapeador mapeador : mapeadores) {
            if (mapeador.soporta(contenido)) {
                return mapeador;
            }
        }
        throw new IllegalArgumentException("No hay un mapeador registrado para " + contenido.getClass().getSimpleName());
    }

    private ContenidoTipoMapeador obtenerMapeadorPorTipo(String tipo) {
        for (ContenidoTipoMapeador mapeador : mapeadores) {
            if (mapeador.obtenerTipo().equals(tipo)) {
                return mapeador;
            }
        }
        throw new IllegalStateException("Tipo de contenido desconocido: " + tipo);
    }

    private Contenido construirContenido(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String titulo = resultSet.getString("titulo");
        Genero genero = Genero.valueOf(resultSet.getString("genero"));
        CalidadStreaming calidad = CalidadStreaming.valueOf(resultSet.getString("calidad"));
        int duracion = resultSet.getInt("duracion_minutos");
        String tipo = resultSet.getString("tipo");
        String atributosExtra = resultSet.getString("atributos_extra");

        ContenidoTipoMapeador mapeador = obtenerMapeadorPorTipo(tipo);
        return mapeador.construir(id, titulo, genero, calidad, duracion, atributosExtra);
    }
}

