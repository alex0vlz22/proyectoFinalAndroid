package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Foro;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceForo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewClaseEstudiante extends AppCompatActivity {

    // Junior url
    //final String url = "http://192.168.1.92:1000";
    // Malejo url
    final String url = "http://192.168.1.5:1000";

    List<Foro> listaForos;
    int idClase;
    int idEstudiante;
    ListView lstForos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clase_estudiante);
        lstForos = (ListView) findViewById(R.id.lstViewForos);

        getSupportActionBar().hide();
        Bundle b = getIntent().getExtras();
        idClase = b.getInt("idClase");
        idEstudiante = b.getInt("idEstudiante");

        llenarLista();
    }

    private void llenarLista() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceForo serviceForo = retrofit.create(ServiceForo.class);
        Call<List<Foro>> foros = serviceForo.listarForosPorClase(idClase);
        foros.enqueue(new Callback<List<Foro>>() {
            @Override
            public void onResponse(Call<List<Foro>> call, Response<List<Foro>> response) {
                try {
                    if (response.isSuccessful()) {
                        listaForos = response.body();
                        if (listaForos.size() != 0) {

                            List<String> nombresForos = new ArrayList<>();
                            for (int i = 0; i < listaForos.size(); i++) {
                                nombresForos.add(listaForos.get(i).toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, nombresForos);
                            lstForos.setAdapter(adapter);

                            lstForos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplicationContext(), viewForoEstudiante.class);
                                    intent.putExtra("idForo", listaForos.get(i).getId());
                                    intent.putExtra("idClase", idClase);
                                    intent.putExtra("idEstudiante", idEstudiante);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            String listaVacia[] = {"No has registrado Foros aún."};
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, listaVacia);
                            lstForos.setAdapter(adapter);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Foro>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falló.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewListadoClasesEstudiante.class);
        intent.putExtra("estudianteId", idEstudiante);
        startActivity(intent);
    }
}