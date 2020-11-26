package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.EstudianteClase;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceEstudianteClase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewListadoClasesEstudiante extends AppCompatActivity {

    int idEstudiante;
    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    List<EstudianteClase> lista = new ArrayList<EstudianteClase>();
    ListView listaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listado_clases_estudiante);

        listaView = (ListView) findViewById(R.id.jlstMisClases);

        Bundle bundle = getIntent().getExtras();
        idEstudiante = bundle.getInt("estudianteId");

        getSupportActionBar().hide();
        llenarLista();

    }

    private void llenarLista() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceEstudianteClase serviceEstudianteClase = retrofit.create(ServiceEstudianteClase.class);
        Call<List<EstudianteClase>> listaClases = serviceEstudianteClase.listarPorIdEstudiante(idEstudiante);
        listaClases.enqueue(new Callback<List<EstudianteClase>>() {
            @Override
            public void onResponse(Call<List<EstudianteClase>> call, Response<List<EstudianteClase>> response) {
                try {
                    if (response.isSuccessful()) {
                        lista = response.body();
                        if (lista.size() != 0) {
                            ArrayAdapter<EstudianteClase> adapter = new ArrayAdapter<EstudianteClase>(ViewListadoClasesEstudiante.this,
                                    android.R.layout.simple_list_item_1, lista);
                            listaView.setAdapter(adapter);
                            listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                                    Intent intent = new Intent(getApplicationContext(), ViewClaseEstudiante.class);
                                    intent.putExtra("idClase", lista.get(i).getId());
                                    intent.putExtra("idEstudiante", idEstudiante);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            String[] semilista = {"No perteneces a una clase aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewListadoClasesEstudiante.this,
                                    android.R.layout.simple_list_item_1, semilista);
                            listaView.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<EstudianteClase>> call, Throwable t) {
                Toast.makeText(ViewListadoClasesEstudiante.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewEstudiante.class);
        i.putExtra("estudianteId", idEstudiante);
        startActivity(i);
    }
}