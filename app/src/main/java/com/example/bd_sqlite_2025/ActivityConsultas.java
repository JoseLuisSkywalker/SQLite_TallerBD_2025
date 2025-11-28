package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import db.EscuelaBD;
import entities.Alumno;

public class ActivityConsultas extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Alumno> datos = null;

    EditText cajaFiltro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        cajaFiltro = findViewById(R.id.cajaBuscar);
        cajaFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null){
                    ((CustomAdapter)adapter).filtrar(s.toString());
                }
            }
        });

        recyclerView = findViewById(R.id.recycleview_alumnos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        EscuelaBD bd = EscuelaBD.getAppDatabase(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                datos = (ArrayList<Alumno>) bd.alumnoDAO().mostrarTodos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomAdapter(datos);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private ArrayList<Alumno> localDataSet;
// examensito
    private ArrayList<Alumno> listaOriginal;
    //
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.textview_alumno);
        }
        public TextView getTextView(){return textView;}
    }

    public CustomAdapter(ArrayList<Alumno> dataset){
        localDataSet = dataset;
        //examensito
        listaOriginal = new ArrayList<>(dataset);
        //
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.textview_recycleview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(localDataSet.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void filtrar(String texto){
        texto = texto.toLowerCase();

        localDataSet.clear();

        if(texto.length() == 0){
            localDataSet.addAll(listaOriginal);
        } else {
            for(Alumno a : listaOriginal){
                boolean coincideNombre = a.getNombre().toLowerCase().contains(texto);
                boolean coincideNC = a.getNumControl().toLowerCase().contains(texto);

                if(coincideNombre || coincideNC){
                    localDataSet.add(a);
                }
            }
        }

        notifyDataSetChanged();
    }
}















