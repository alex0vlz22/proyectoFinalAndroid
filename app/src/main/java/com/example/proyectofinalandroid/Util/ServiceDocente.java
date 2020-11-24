package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Docente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceDocente {

    @POST("/guardarDocente")
    public Call<Docente> guardar(@Body Docente docente);

    @GET("/buscarDocente/{documento}")
    public Call<Docente> buscar(@Path("documento") long documento);

    @PUT("/modificarDocente/{id}")
    public Call<Docente> modificar(@Body Docente docente, @Path("id") int id);

    @DELETE("/eliminarDocente/{documento}")
    public Call<Docente> eliminar(@Path("documento") long documento);

    @GET("/docentes")
    public Call<List<Docente>> listar();

    @GET("/buscarDocentePorCorreo/{correoDocente}")
    public Call<Docente> buscarPorCorreo(@Path("correoDocente") String correo);

    @GET("/buscarPorId/{id}")
    public Call<Docente> buscarPorId(@Path("id") int id);

}
