package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Estudiante;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEstudiante {

    @POST("/guardarEstudiante")
    public Call<Estudiante> guardar(@Body Estudiante estudiante);

    @GET("/buscarEstudiante/{documento}")
    public Call<Estudiante> buscar(@Path("documento") long documento);

    @GET("/buscarEstudianteId/{id}")
    public Call<Estudiante> buscarPorId(@Path("id") int id);

    @PUT("/modificarEstudiante")
    public Call<Estudiante> modificar(@Body Estudiante estudiante);

    @DELETE("/eliminarEstudiante/{documento}")
    public Call<Estudiante> eliminar(@Path("documento") long documento);

    @GET("/listarEstudiantes")
    public Call<List<Estudiante>> listarEStudiantes();

    @GET("/buscarEstudiantePorCorreo/{correo}")
    public Call<Estudiante> buscarPorCorreo(@Path("correo") String correo);

}
