package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Clase;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceClase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListadoClases extends AppCompatActivity {

    int idDocente;
    ListView lista;
    List listaClases;
    long documento;

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_clases);

        lista = (ListView) findViewById(R.id.jlstClases);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("idDocente");
        documento = b.getLong("documento");

        llenarLista();
        getSupportActionBar().hide();

    }

    private void llenarLista() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceClase serviceClase = retrofit.create(ServiceClase.class);
        Call<List<Clase>> clases = serviceClase.buscarPorDocente(idDocente);
        clases.enqueue(new Callback<List<Clase>>() {
            @Override
            public void onResponse(Call<List<Clase>> call, Response<List<Clase>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaClases = response.body();
                        if (listaClases.size() != 0) {
                            ArrayAdapter<Foro> adapter = new ArrayAdapter<Foro>(ListadoClases.this,
                                    android.R.layout.simple_list_item_1, listaClases);
                            lista.setAdapter(adapter);

                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(ListadoClases.this, listaClases.get(i).toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            String listaVacia[] = {"No has registrado clases aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListadoClases.this,
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
            public void onFailure(Call<List<Clase>> call, Throwable t) {
                Toast.makeText(ListadoClases.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewClases.class);
        i.putExtra("docenteId", idDocente);
        i.putExtra("documento", documento);
        startActivity(i);
    }
}