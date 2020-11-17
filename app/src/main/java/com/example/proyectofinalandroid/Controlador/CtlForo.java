package com.example.proyectofinalandroid.Controlador;

import com.example.proyectofinalandroid.Exception.OcurrioUnErrorGuardandoException;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.Util.ServiceForo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CtlForo {
    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.3:1000";

    // esta variable es creada para comparar si su valor aún es 0 en unos lugares del código donde
    // no puedo retornar false ni una excepción, comparo si esta variable es diferente de 0, de ser
    // así, el método en general lo controlaré como 'no hubo una respuesta esperada'
    int auxValidaciones = 0;

    public boolean registrarse(Foro foro, int aux) throws OcurrioUnErrorGuardandoException {
        // siempre que llame este método le enviaré por defecto el 0 como aux
        this.auxValidaciones = aux;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<Foro> foroAfter = serviceForo.guardar(foro);
        foroAfter.enqueue(new Callback<Foro>() {
            @Override
            public void onResponse(Call<Foro> call, Response<Foro> response) {
                try {
                    if (response.isSuccessful()) {
                        Foro f = response.body();
                        if (f == null) {
                            auxValidaciones++;
                            throw new OcurrioUnErrorGuardandoException("No se ha podido guardar.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    auxValidaciones++;
                }
            }

            @Override
            public void onFailure(Call<Foro> call, Throwable t) {
                auxValidaciones++;

            }
        });

        if (auxValidaciones != 0)
            return false;
        else
            return true;
    }
}
