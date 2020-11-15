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

public class ListadoForos extends AppCompatActivity {
    ListView lstForos;
    CtlForo controladorForo;
    int idDocente;
    // Junior url
    //final String url = "http://192.168.1.92:1000";
    // Malejo url
    final String url = "http://192.168.1.3:1000";
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

                            List<String> nombresForos = new ArrayList<>();
                            for (int i = 0; i < listaForos.size(); i++) {
                                nombresForos.add(listaForos.get(i).toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListadoForos.this,
                                    android.R.layout.simple_list_item_1, nombresForos);
                            lstForos.setAdapter(adapter);

                            lstForos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplicationContext(), ViewForoDocente.class);
                                    intent.putExtra("idForo", listaForos.get(i).getId());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            String listaVacia[] = {"No has registrado Foros aún."};
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewDocente.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }
}