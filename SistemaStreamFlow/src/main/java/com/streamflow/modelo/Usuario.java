package com.streamflow.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String cedula;
    private String nombres;
    private String email;
    private List<Genero> generosFavoritos;

    public Usuario(String cedula, String nombres, String email) {
        validarCedula(cedula);
        validarNombres(nombres);
        this.cedula = cedula;
        this.nombres = nombres;
        this.email = email;
        this.generosFavoritos = new ArrayList<>();
    }

    private void validarCedula(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) {
            throw new IllegalArgumentException("La cedula debe tener 10 digitos numericos");
        }
    }

    private void validarNombres(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacios");
        }
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        validarNombres(nombres);
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Genero> getGenerosFavoritos() {
        return generosFavoritos;
    }

    public void agregarGeneroFavorito(Genero genero) {
        generosFavoritos.add(genero);
    }
}

