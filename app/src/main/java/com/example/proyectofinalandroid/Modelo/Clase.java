package com.example.proyectofinalandroid.Modelo;

public class Clase {

    private int id;
    private String grado;
    private int idDocente;

    public Clase() {
    }

    public Clase(int id, String grado, int idDocente) {
        this.id = id;
        this.grado = grado;
        this.idDocente = idDocente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    @Override
    public String toString() {
        return grado;
    }
}
