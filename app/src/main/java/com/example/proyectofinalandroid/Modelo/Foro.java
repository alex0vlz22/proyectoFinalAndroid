package com.example.proyectofinalandroid.Modelo;

public class Foro {

    private int id;
    private String titulo, descripcion;
    private boolean activo;
    private int limiteParticipaciones;
    private Docente docente;


    public Foro() {

    }

    public Foro(String titulo, String descripcion, boolean activo, int limiteParticipaciones, Docente docente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.limiteParticipaciones = limiteParticipaciones;
        this.docente = docente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getLimiteParticipaciones() {
        return limiteParticipaciones;
    }

    public void setLimiteParticipaciones(int limiteParticipaciones) {
        this.limiteParticipaciones = limiteParticipaciones;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
