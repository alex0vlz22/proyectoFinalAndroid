package com.example.proyectofinalandroid.Util;

import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.Modelo.Participacion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceParticipacion {

    @POST("/guardarParticipacion")
    public Call<Participacion> guardar(@Body Participacion participacion);

    @GET("/buscarParticipacion/{id}")
    public Call<Participacion> buscar(@Path("id") int id);

    @GET("/listarParticipaciones")
    public Call<List<Participacion>> listarForosPorDocente();

    @GET("/listarParticipacionesForo/{idForo}")
    public Call<List<Participacion>> listarParticipacionesPorForo(@Path("idForo") int idForo);

    @GET("/listarParticipacionesPorParticipante/{idParticipante}")
    public Call<List<Participacion>> listarForosPorParticipante(@Path("idParticipante") int idParticipante);

    @GET("/listarParticipacionPorParticipanteEnForo/{idParticipante}/{idForo}")
    public List<Participacion> listarParticipacionesPorParticipanteEnForo(@Path("idParticipante") int idParticipante, @Path("idForo") int idForo);

    @DELETE("/eliminarParticipacion/{id}")
    public Call<Participacion> eliminar(@Path("id") int id);

}
