package com.example.proyectofinalandroid.Modelo;

public class Clase {

    private int id;
    private String grado, codigo, nombre;
    private int idDocente;

    public Clase() {
    }

    public Clase(int id, String grado, String codigo, String nombre, int idDocente) {
        this.id = id;
        this.grado = grado;
        this.codigo = codigo;
        this.nombre = nombre;
        this.idDocente = idDocente;
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

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    @Override
    public String toString() {
        return "Grado: " + grado + ", código: " + codigo;
    }
}
