package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinalandroid.Modelo.Clase;
import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceClase;
import com.example.proyectofinalandroid.Util.ServiceDocente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewRegistroClase extends AppCompatActivity {

    Spinner letra, numero;
    Button guardar;
    int idDocente;
    Clase claseNueva = new Clase();
    Docente docenteParaClase = new Docente();
    EditText codigo, nombre;
    String codigoGenerado;
    long documento;

    // Junior url
    final String url = "http://192.168.1.2:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_clases);

        Bundle b = getIntent().getExtras();
        idDocente = b.getInt("idDocente");
        documento = b.getLong("documento");

        letra = (Spinner) findViewById(R.id.jSpnLetra);
        numero = (Spinner) findViewById(R.id.jSpnNumero);
        guardar = (Button) findViewById(R.id.jBtnGuardar);
        codigo = (EditText) findViewById(R.id.jtxtCodigo);
        nombre = (EditText) findViewById(R.id.jtxtNombreClase);
        codigoGenerado = "";

        llenarDocenteClase();
        iniciarSpinners();
        generarCodigo(0);
        getSupportActionBar().hide();
    }

    private void generarCodigo(int x) {
        if (x == 3) {
            codigo.setText(codigoGenerado);
            return;
        } else {
            Random rnd = new Random();
            codigoGenerado += (char) ('a' + rnd.nextInt(26));
            codigoGenerado += 0 + rnd.nextInt(10);
            generarCodigo(x + 1);
        }
    }

    private void llenarDocenteClase() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
        Call<Docente> docente = serviceDocente.buscarPorId(idDocente);
        docente.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {
                try {
                    if (response.isSuccessful()) {
                        Docente doc = response.body();
                        docenteParaClase = doc;
                        return;
                    } else {
                        Toast.makeText(ViewRegistroClase.this, "Respuesta no exitosa.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                Toast.makeText(ViewRegistroClase.this, "Falló.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarSpinners() {
        // Se llenan los spinners
        String gradosNumeros[] = {"Seleccionar", "5", "6", "7", "8", "9", "10", "11"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosNumeros);
        numero.setAdapter(adapter);

        String gradosLetras[] = {"Seleccionar", "A", "B", "C", "D", "E", "F", "G"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosLetras);
        letra.setAdapter(adapter2);
    }

    public void guardar(View view) {
        if (validarCampos()) {
            Toast.makeText(this, "Completa los campos adecuadamente.", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            ServiceClase serviceClase = retrofit.create(ServiceClase.class);
            Call<Clase> clase = serviceClase.buscar(numero.getSelectedItem().toString() +
                    letra.getSelectedItem().toString(), idDocente);
            clase.enqueue(new Callback<Clase>() {
                @Override
                public void onResponse(Call<Clase> call, Response<Clase> response) {
                    try {
                        if (response.isSuccessful()) {
                            Clase cls = response.body();
                            imprimir("Ya has registrado este grado previamente.");
                            return;
                        } else {
                            claseNueva.setGrado(numero.getSelectedItem().toString() +
                                    letra.getSelectedItem().toString());
                            claseNueva.setIdDocente(docenteParaClase.getId());
                            claseNueva.setCodigo(codigo.getText().toString());
                            claseNueva.setNombre(nombre.getText().toString());
                            Call<Clase> clase = serviceClase.guardar(claseNueva);
                            clase.enqueue(new Callback<Clase>() {
                                @Override
                                public void onResponse(Call<Clase> call, Response<Clase> response) {
                                    try {
                                        if (response.isSuccessful()) {
                                            imprimir("La clase ha sido registrada.");
                                            limpiarCampos();
                                            return;
                                        } else {
                                            imprimir("La clase no pudo ser creada.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Clase> call, Throwable t) {
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        imprimir(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Clase> call, Throwable t) {
                    Toast.makeText(ViewRegistroClase.this, "Ya has registrado este grado.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void limpiarCampos() {
        numero.setSelection(0);
        letra.setSelection(0);
        codigo.setText("");
        codigoGenerado = "";
        nombre.setText("");
        generarCodigo(0);
    }

    private void imprimir(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean validarCampos() {
        if (letra.getSelectedItem().toString().equals("Seleccionar") || numero.getSelectedItem().equals("Seleccionar")
                || nombre.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewClases.class);
        intent.putExtra("docenteId", idDocente);
        intent.putExtra("documento", documento);
        startActivity(intent);
    }
}