package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.EstudianteClase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceEstudianteClase {

    @POST("/guardarEstudianteClase")
    public Call<EstudianteClase> guardar(@Body EstudianteClase estudianteClase);

}
