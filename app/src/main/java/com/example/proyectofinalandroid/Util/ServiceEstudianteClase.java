package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.EstudianteClase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceEstudianteClase {

    @POST("/guardarEstudianteClase")
    public Call<EstudianteClase> guardar(@Body EstudianteClase estudianteClase);

    @GET("/listarClasesPorEstudiante/{idEstudiante}")
    public Call<List<EstudianteClase>> listarPorIdEstudiante(@Path("idEstudiante") int idEstudiante);

}
