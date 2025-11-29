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

public class ActivityBajas extends Activity {

    EditText cajaNumControl;
    Button btnEliminar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        cajaNumControl = findViewById(R.id.caja_num_control_bajas);
        btnEliminar = findViewById(R.id.button);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nc = cajaNumControl.getText().toString().trim();

                if (nc.isEmpty()) {
                    Toast.makeText(ActivityBajas.this, "Ingresa un número de control", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        EscuelaBD bd = EscuelaBD.getAppDatabase(ActivityBajas.this);

                        Alumno alumno = bd.alumnoDAO().buscarPorNumControl(nc);

                        if (alumno != null) {
                            bd.alumnoDAO().eliminarAlumno(alumno);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityBajas.this, "Alumno eliminado", Toast.LENGTH_SHORT).show();
                                    cajaNumControl.setText("");
                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityBajas.this, "No existe un alumno con ese número de control", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}