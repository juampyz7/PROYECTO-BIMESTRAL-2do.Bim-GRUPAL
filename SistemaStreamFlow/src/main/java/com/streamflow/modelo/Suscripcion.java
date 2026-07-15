package com.streamflow.modelo;

import java.time.LocalDate;

public class Suscripcion {

    private int id;
    private String cedulaUsuario;
    private CalidadStreaming calidad;
    private double costoMensual;
    private LocalDate fechaInicio;
    private boolean activa;

    public Suscripcion(int id, String cedulaUsuario, CalidadStreaming calidad, double costoMensual, LocalDate fechaInicio) {
        this.id = id;
        this.cedulaUsuario = cedulaUsuario;
        this.calidad = calidad;
        this.costoMensual = costoMensual;
        this.fechaInicio = fechaInicio;
        this.activa = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public CalidadStreaming getCalidad() {
        return calidad;
    }

    public void setCalidad(CalidadStreaming calidad) {
        this.calidad = calidad;
    }

    public double getCostoMensual() {
        return costoMensual;
    }

    public void setCostoMensual(double costoMensual) {
        this.costoMensual = costoMensual;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
