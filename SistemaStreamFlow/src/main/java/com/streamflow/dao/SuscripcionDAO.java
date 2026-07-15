package com.streamflow.dao;

import com.streamflow.modelo.Suscripcion;

import java.util.List;

public interface SuscripcionDAO {

    void guardar(Suscripcion suscripcion);

    Suscripcion buscarPorId(int id);

    List<Suscripcion> listarPorUsuario(String cedulaUsuario);

    List<Suscripcion> listarTodas();

    void actualizar(Suscripcion suscripcion);

    void eliminar(int id);
}
