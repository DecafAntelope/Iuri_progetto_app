package com.Iuri.iuri_progetto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEsercizio extends RecyclerView.Adapter<AdapterEsercizio.MyViewHolder>{
    Context context;
    ArrayList<Esercizio> arrayEsercizi;

    public AdapterEsercizio(Context c,ArrayList<Esercizio> l)
    {
        context = c;
        arrayEsercizi = l;
    }

    @NonNull
    @Override
    public AdapterEsercizio.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.esercizi_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEsercizio.MyViewHolder holder, int position) {
        holder.nome.setText(arrayEsercizi.get(position).nome);
        holder.minuti.setText(String.valueOf(arrayEsercizi.get(position).minuti));
        holder.secondi.setText(String.valueOf(arrayEsercizi.get(position).secondi));
        holder.serie.setText(String.valueOf(arrayEsercizi.get(position).serie));
        holder.ripetizioni.setText(String.valueOf(arrayEsercizi.get(position).ripetizioni));
    }

    @Override
    public int getItemCount() {
        return arrayEsercizi.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome,serie,ripetizioni,minuti,secondi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView)itemView.findViewById(R.id.adapterEsercizio);
            secondi = (TextView)itemView.findViewById(R.id.adapterSecondi);
            minuti = (TextView)itemView.findViewById(R.id.adapterMinuti);
            ripetizioni = (TextView)itemView.findViewById(R.id.adapterReps);
            serie = (TextView)itemView.findViewById(R.id.adapterSerie);
        }
    }
}
