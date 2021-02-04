package com.Iuri.iuri_progetto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCommento extends RecyclerView.Adapter<AdapterCommento.MyViewHolder>{
    ArrayList<Commento> listComm;
    Context context;

    public AdapterCommento(ArrayList<Commento> listComm, Context context) {
        this.listComm = listComm;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCommento.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.commento,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCommento.MyViewHolder holder, int position) {
        holder.ut.setText(listComm.get(position).autore);
        holder.txt.setText(listComm.get(position).testo);
    }

    @Override
    public int getItemCount() {
        return listComm.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView ut,txt;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ut = (TextView) itemView.findViewById(R.id.txtComUtente);
            txt = (TextView) itemView.findViewById(R.id.txtComText);

        }
    }
}
