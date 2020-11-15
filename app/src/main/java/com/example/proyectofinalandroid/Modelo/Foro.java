package com.example.proyectofinalandroid.Modelo;

public class Foro {

    private int id;
    private String titulo, descripcion;
    private boolean activo;
    private int limiteParticipaciones;
    private int idDocente;
    private String grado;

    public Foro() {

    }

    public Foro(String titulo, String descripcion, boolean activo, int limiteParticipaciones, int idDocente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.limiteParticipaciones = limiteParticipaciones;
        this.idDocente = idDocente;
    }

    public Foro(int id, String titulo, String descripcion, boolean activo, int limiteParticipaciones, int idDocente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.limiteParticipaciones = limiteParticipaciones;
        this.idDocente = idDocente;
    }

    public Foro(String titulo, String descripcion, boolean activo, int limiteParticipaciones, int idDocente, String grado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.limiteParticipaciones = limiteParticipaciones;
        this.idDocente = idDocente;
        this.grado = grado;
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

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    @Override
    public String toString() {
        String estado = "inactivo";
        if (activo) {
            estado = "activo";
        }
        return "Titulo: " + titulo + ", estado: " + estado;
    }
}
