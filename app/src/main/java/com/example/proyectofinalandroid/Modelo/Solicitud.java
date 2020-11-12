package com.example.proyectofinalandroid.Modelo;

public class Solicitud {

    private int id, idEstudiante, idDocente, idClase;
    private String nombreClase, codigo, nombreEstudiante, gradoEstudiante, gradoClase;

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

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getGradoEstudiante() {
        return gradoEstudiante;
    }

    public void setGradoEstudiante(String gradoEstudiante) {
        this.gradoEstudiante = gradoEstudiante;
    }

    public String getGradoClase() {
        return gradoClase;
    }

    public void setGradoClase(String gradoClase) {
        this.gradoClase = gradoClase;
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
        return "Clase: " + nombreClase + ", Grado: " + gradoClase;
    }
}
