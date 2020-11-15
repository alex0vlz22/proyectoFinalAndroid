package com.example.proyectofinalandroid.Modelo;

public class Foro {

    private int id;
    private String titulo, descripcion;
    private boolean activo;
    private int limiteParticipaciones;
    private int idDocente;
    private int idClase;

    public Foro() {

    }

    public Foro(String titulo, String descripcion, boolean activo, int limiteParticipaciones, int idDocente, int idClase) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.limiteParticipaciones = limiteParticipaciones;
        this.idDocente = idDocente;
        this.idClase = idClase;
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

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    @Override
    public String toString() {
        return "Titulo= " + titulo +
                " , descripcion= " + descripcion +
                ", activo= " + activo +
                ", limite de participaciones= " + limiteParticipaciones +
                ", idDocente=" + idDocente +
                ", idClase='" + idClase + '\'' +
                '}';
    }
}
