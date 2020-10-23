package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.R;

import java.util.Calendar;


public class ViewRegistroEstudiante extends AppCompatActivity{

    EditText calendario;
    Spinner gradoNumero, gradoLetra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro);

        calendario = (EditText) findViewById(R.id.jtxtFecha);
        gradoNumero = (Spinner) findViewById(R.id.jSpnGradoNumero);
        gradoLetra = (Spinner) findViewById(R.id.jSpnGradoLetra);

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();

        generarCalendar();
        llenarSpinners();
    }

    private void llenarSpinners() {
        String gradosNumeros[] = {"Seleccionar", "5", "6", "7", "8", "9", "10", "11"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosNumeros);
        gradoNumero.setAdapter(adapter);

        String gradosLetras[] = {"Seleccionar", "A", "B", "C", "D", "E", "F", "G"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradosLetras);
        gradoLetra.setAdapter(adapter2);

        /* ----------- CODIGO PARA GENERAR LLENAR SPINNER, TOMADO DE PROYECTO VIEJO ---------------

        List<> listaMarcas = ctlMarca.listar();
        List<> listaNombresMarcas = new ArrayList<String>();
        for (int i = 0; i < listaMarcas.size(); i++) {
            listaNombresMarcas.add(listaMarcas.get(i).getNombre());
        }
        adapterMarcas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNombresMarcas);
        marca.setAdapter(adapterMarcas); */
    }

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewRegistroEstudiante.this,
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

    // método para manipular el botón 'atrás' del dispositivo
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}