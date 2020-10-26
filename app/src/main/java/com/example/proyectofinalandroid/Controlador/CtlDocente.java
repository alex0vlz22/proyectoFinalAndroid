package com.example.proyectofinalandroid.Controlador;

import android.app.Service;
import android.widget.Toast;

import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Util.ServiceDocente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CtlDocente {

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    // final String url = "http://192.168.1.4:1000";

    // esta variable es creada para comparar si su valor aún es 0 en unos lugares del código donde
    // no puedo retornar false ni una excepción, comparo si esta variable es diferente de 0, de ser
    // así, el método en general lo controlaré como 'no hubo una respuesta esperada'
    int auxValidaciones = 0;

    public boolean registrarse(Docente docente, int aux) throws OcurrioUnErrorGuardandoException{
        // siempre que llame este método le enviaré por defecto el 0 como aux
        this.auxValidaciones = aux;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
        Call<Docente> docenteAfter = serviceDocente.guardar(docente);
        docenteAfter.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {
                try {
                    if (response.isSuccessful()) {
                        Docente doc = response.body();
                        if (doc == null) {
                            auxValidaciones++;
                            throw new OcurrioUnErrorGuardandoException("No se ha podido guardar.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                auxValidaciones++;
            }
        });

        if (auxValidaciones != 0)
            return false;
        else
            return true;
    }
}
