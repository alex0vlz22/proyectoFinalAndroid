package com.example.proyectofinalandroid.Controlador;

import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CtlEstudiante {

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    // esta variable es creada para comparar si su valor aún es 0 en unos lugares del código donde
    // no puedo retornar false ni una excepción, comparo si esta variable es diferente de 0, de ser
    // así, el método en general lo controlaré como 'no hubo una respuesta esperada'
    int auxValidaciones = 0;

    public boolean registrarse(Estudiante estudiante, int aux) throws OcurrioUnErrorGuardandoException {
        // siempre que llame este método le enviaré por defecto el 0 como aux
        this.auxValidaciones = aux;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceEstudiante serviceEstudiante = retrofit.create(ServiceEstudiante.class);
        Call<Estudiante> estAfter = serviceEstudiante.guardar(estudiante);
        estAfter.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                try {
                    if (response.isSuccessful()) {
                        Estudiante est = response.body();
                        if (est == null) {
                            auxValidaciones++;
                            throw new OcurrioUnErrorGuardandoException("No se ha podido guardar.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                auxValidaciones++;
            }
        });

        if (auxValidaciones != 0)
            return false;
        else
            return true;
    }

}
