package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Solicitud;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceSolicitud {

    @POST("/guardarSolicitud")
    public Call<Solicitud> guardarSolicitud(@Body Solicitud solicitud);

    @GET("/buscarSolicitudPorIdEstudiante/{idEstudiante}")
    public Call<Solicitud> buscarPorIdEstudiante(@Path("idEstudiante") int idEstudiante);

    @GET("/buscarSolicitudPorIdDocente/{idDocente}")
    public Call<Solicitud> buscarPorIdDocente(@Path("idDocente") int idDocente);

}
