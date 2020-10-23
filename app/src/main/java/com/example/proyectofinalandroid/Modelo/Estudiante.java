package com.example.proyectofinalandroid.Modelo;

import java.util.Date;

public class Estudiante extends Docente{

    private String grado;

    public Estudiante() {
    }

    public Estudiante(int id, long documento, String nombre, String apellido, long telefono, String correo, String contrasena, Date fechaNacimiento, String grado) {
        super(id, documento, nombre, apellido, telefono, correo, contrasena, fechaNacimiento);
        this.grado = grado;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "grado='" + grado + '\'' +
                '}';
    }

}
