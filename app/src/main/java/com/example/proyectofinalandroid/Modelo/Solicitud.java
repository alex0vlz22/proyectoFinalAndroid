package com.example.proyectofinalandroid.Modelo;

public class Solicitud {

    private int id, idEstudiante, idDocente;
    private String nombreClase, codigo;

    public Solicitud() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    @Override
    public String toString() {
        return "Clase: " + nombreClase;
    }
}
