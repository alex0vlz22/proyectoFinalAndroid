package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Clase;
import com.example.proyectofinalandroid.Modelo.Docente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceClase {

    @POST("/guardarClase")
    public Call<Clase> guardar(@Body Clase clase);

    @GET("/buscarClase/{grado}/{id}")
    public Call<Clase> buscar(@Path("grado") String grado, @Path("id") int id);

    @PUT("/modificarClase")
    public Call<Clase> modificar(@Body Clase clase);

    @DELETE("/eliminarClase/{id}")
    public Call<Clase> eliminar(@Path("id") int id);

    @GET("/clases")
    public Call<List<Clase>> listar();

    @GET("/listarPorDocente/{idDocente}")
    public Call<List<Clase>> buscarPorDocente(@Path("idDocente") int idDocente);

    @GET("/buscarPorCodigo/{codigo}")
    public Call<Clase> buscarPorCodigo(@Path("codigo") String codigo);

}
