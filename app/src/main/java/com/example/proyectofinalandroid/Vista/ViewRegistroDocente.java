package com.example.proyectofinalandroid.Vista;

import androidx.appcompat.app.AppCompatActivity;
// Éste import tuve que agregarlo manualmente
import com.example.proyectofinalandroid.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class ViewRegistroDocente extends AppCompatActivity {

    EditText calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registro_docente);

        calendario = (EditText) findViewById(R.id.jtxtFecha);

        // Línea para ocultar la barra de navegación por defecto del android studio
        getSupportActionBar().hide();

        generarCalendar();

    }

    private void generarCalendar() {
        this.calendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                DatePickerDialog mDatePicker = new DatePickerDialog(ViewRegistroDocente.this,
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}