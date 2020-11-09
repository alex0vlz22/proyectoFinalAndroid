package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Controlador.CtlForo;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.Modelo.Estudiante;
import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;
import com.example.proyectofinalandroid.Util.ServiceEstudiante;
import com.example.proyectofinalandroid.Util.ServiceForo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListadoForos extends AppCompatActivity {
    ListView lstForos;
    CtlForo controladorForo;
    int idDocente;
    // Junior url
       final String url = "http://192.168.1.92:1000";
    // Malejo url
    // final String url = "http://192.168.1.9:1000";
    long documento;
    List<Foro> listaForos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_foros);
        lstForos = (ListView) findViewById(R.id.lstView);
        controladorForo = new CtlForo();
        Bundle b = getIntent().getExtras();
        documento = b.getLong("documento");
        idDocente = b.getInt("docenteId");
        getSupportActionBar().hide();
        llenarLista();
    }

    private void llenarLista() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<List<Foro>> foros = serviceForo.listarForosPorDocente(idDocente);
        foros.enqueue(new Callback<List<Foro>>() {
            @Override
            public void onResponse(Call<List<Foro>> call, Response<List<Foro>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaForos = response.body();
                        if (listaForos.size() != 0) {
                            ArrayAdapter<Foro> adapter = new ArrayAdapter<Foro>(ListadoForos.this,
                                    android.R.layout.simple_list_item_1, listaForos);
                            lstForos.setAdapter(adapter);

                            lstForos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(ListadoForos.this, listaForos.get(i).getTitulo().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            String listaVacia[] = {"No has registrado puntos aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListadoForos.this,
                                    android.R.layout.simple_list_item_1, listaVacia);
                            lstForos.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Foro>> call, Throwable t) {
                Toast.makeText(ListadoForos.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}