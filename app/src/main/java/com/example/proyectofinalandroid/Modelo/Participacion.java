package com.example.proyectofinalandroid.Modelo;

public class Participacion {

    private int id;
    private String descripcion;
    private int idParticipante;
    private int idForo;

    public Participacion(String descripcion, int idParticipante, int idForo) {
        this.descripcion = descripcion;
        this.idParticipante = idParticipante;
        this.idForo = idForo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public int getIdForo() {
        return idForo;
    }

    public void setIdForo(int idForo) {
        this.idForo = idForo;
    }
}
