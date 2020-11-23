package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinalandroid.Modelo.Docente;
import com.example.proyectofinalandroid.R;
import com.example.proyectofinalandroid.Util.ServiceDocente;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewEditarPerfilDocente extends AppCompatActivity {

    int idDocente;
    long documentoRecibido;
    EditText documento, nombre, apellido, telefono, correo, contrasena, calendario;

    // Junior url
    final String url = "http://192.168.1.92:1000";
    // Malejo url
    //final String url = "http://192.168.1.5:1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_editar_perfil_docente);

        inicializarComponentes();

    }

    private void inicializarComponentes() {
        Bundle bundle = getIntent().getExtras();
        idDocente = bundle.getInt("idDocente");
        documentoRecibido = bundle.getLong("documento");

        documento = (EditText) findViewById(R.id.campoTitulo);
        nombre = (EditText) findViewById(R.id.jtxtDescripcion);
        apellido = (EditText) findViewById(R.id.jtxtApellido);
        telefono = (EditText) findViewById(R.id.jtxtLimite);
        correo = (EditText) findViewById(R.id.jtxtCorreo);
        contrasena = (EditText) findViewById(R.id.jtxtContraseña);
        calendario = (EditText) findViewById(R.id.jtxtFecha);

        getSupportActionBar().hide();
        generarCalendar();
        llenarDocente();
    }

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewEditarPerfilDocente.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                calendario.setText(selectedyear + "/" + selectedmonth + "/" + selectedday);
                            }
                        }, mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH),
                        mcurrentDate.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    private void llenarDocente() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        ServiceDocente serviceDocente = retrofit.create(ServiceDocente.class);
        Call<Docente> docente = serviceDocente.buscarPorId(idDocente);
        docente.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {
                try {
                    if (response.isSuccessful()) {
                        Docente docenteBuscado = response.body();
                        llenarCamposDocenteBuscado(docenteBuscado);
                        return;
                    } else {
                        imprimir("Algo ocurrió buscando el docente.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imprimir(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                imprimir("Falló.");
            }
        });
    }

    private void llenarCamposDocenteBuscado(Docente docenteBuscado) {
        documento.setText(Long.toString(docenteBuscado.getDocumento()));
        nombre.setText(docenteBuscado.getNombre());
        apellido.setText(docenteBuscado.getApellido());
        telefono.setText(Long.toString(docenteBuscado.getTelefono()));
        correo.setText(docenteBuscado.getCorreo());
        contrasena.setText(docenteBuscado.getContrasena());
        calendario.setText(docenteBuscado.getFechaNacimiento());
    }

    private void imprimir(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewDocente.class);
        intent.putExtra("documento", documentoRecibido);
        intent.putExtra("docenteId", idDocente);
        startActivity(intent);
    }
}