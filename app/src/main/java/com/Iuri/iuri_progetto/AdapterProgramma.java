package com.Iuri.iuri_progetto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterProgramma extends RecyclerView.Adapter<AdapterProgramma.MyViewHolder> {

    Context context;
    ArrayList<Programma> arrayProgrammi;
    OnProgListener listen;

    public AdapterProgramma(Context context, ArrayList<Programma> arrayProgrammi,OnProgListener l) {
        this.context = context;
        this.arrayProgrammi = arrayProgrammi;
        listen = l;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.programmi_recview,parent,false),listen);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.nomeP.setText(arrayProgrammi.get(position).getNomeProgramma());
            holder.nomeA.setText(arrayProgrammi.get(position).getAutore());
    }

    @Override
    public int getItemCount() {
        return arrayProgrammi.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView nomeP,nomeA;
        OnProgListener listener;
        Button comm;
        public MyViewHolder(@NonNull View itemView,OnProgListener list) {
            super(itemView);
            listener=list;
            nomeP = (TextView) itemView.findViewById(R.id.txtnomePList);
            nomeA = (TextView) itemView.findViewById(R.id.txtAutoreList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onProgClick(getAdapterPosition());
        }
    }

    public interface OnProgListener{
        void onProgClick(int position);
    }
}
