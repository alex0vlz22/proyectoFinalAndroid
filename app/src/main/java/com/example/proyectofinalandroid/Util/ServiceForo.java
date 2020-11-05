package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Foro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceForo {

    @POST("/guardarForo")
    public Call<Foro> guardar(@Body Foro foro);

    @GET("/buscarForo/{id}")
    public Call<Foro> buscar(@Path("id") int id);

    @GET("/listarForosDocente/{docenteId}")
    public Call<List<Foro>> listarForosPorDocente(@Path("docenteId") int docenteId);

    @DELETE("/eliminarForo/{id}")
    public Call<Foro> eliminar(@Path("id") int id);

    @GET("/listarForos")
    public Call<List<Foro>> listarForos();
}
