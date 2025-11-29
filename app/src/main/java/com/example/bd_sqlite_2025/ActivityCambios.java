package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.EscuelaBD;
import entities.Alumno;

public class ActivityCambios extends Activity {

    EditText cajaNumControl;
    EditText cajaNuevoNombre;
    Button btnActualizar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        cajaNumControl = findViewById(R.id.editTextText);
        cajaNuevoNombre = findViewById(R.id.editTextText2);
        btnActualizar = findViewById(R.id.button3);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nc = cajaNumControl.getText().toString().trim();
                String nuevoNombre = cajaNuevoNombre.getText().toString().trim();

                if (nc.isEmpty() || nuevoNombre.isEmpty()) {
                    Toast.makeText(ActivityCambios.this, "Llena ambos campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        EscuelaBD bd = EscuelaBD.getAppDatabase(ActivityCambios.this);

                        Alumno alumno = bd.alumnoDAO().buscarPorNumControl(nc);

                        if (alumno != null) {

                            alumno.setNombre(nuevoNombre);
                            bd.alumnoDAO().actualizarAlumno(alumno);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityCambios.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityCambios.this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}