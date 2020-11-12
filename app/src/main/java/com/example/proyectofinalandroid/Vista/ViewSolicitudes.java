package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Solicitud;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceSolicitud;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewSolicitudes extends AppCompatActivity {

    int idDocente;
    List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
    ListView lista;

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    // final String url = "http://192.168.1.9:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solicitudes);

        lista = (ListView) findViewById(R.id.jlstSolicitudes);

        Bundle bundle = getIntent().getExtras();
        idDocente = bundle.getInt("idDocente");

        getSupportActionBar().hide();
        llenarLista();

    }

    private void llenarLista() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceSolicitud serviceSolicitud = retrofit.create(ServiceSolicitud.class);
        Call<List<Solicitud>> listaX = serviceSolicitud.buscarPorIdDocente(idDocente);
        listaX.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaSolicitudes = response.body();
                        if (listaSolicitudes.size() != 0) {
                            ArrayAdapter<Solicitud> adapter = new ArrayAdapter<Solicitud>(ViewSolicitudes.this,
                                    android.R.layout.simple_list_item_1, listaSolicitudes);
                            lista.setAdapter(adapter);

                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(ViewSolicitudes.this, listaSolicitudes.get(i).getCodigo(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            String listaVacia[] = {"No hay solicitudes aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewSolicitudes.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            lista.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Toast.makeText(ViewSolicitudes.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewClases.class);
        intent.putExtra("docenteId", idDocente);
        startActivity(intent);
    }
}