package com.streamflow.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private List<Genero> generosFavoritos;

    public Usuario(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.generosFavoritos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
