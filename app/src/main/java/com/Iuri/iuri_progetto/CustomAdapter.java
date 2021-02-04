package com.Iuri.iuri_progetto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Esercizio> list;
    LayoutInflater inflater;
    public CustomAdapter(Context cont,ArrayList<Esercizio> l)
    {
        this.context=cont;
        this.list = l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view =  LayoutInflater.from(context).inflate(R.layout.esercizi_listview,parent,true);
        //setUpData(view,position);
        View row = convertView;
        if(row==null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row= inflater.inflate(R.layout.esercizi_listview,null);
        }
        TextView esercizio = (TextView) row.findViewById(R.id.adapterEsercizio);
        TextView serie = (TextView) row.findViewById(R.id.adapterSerie);
        TextView ripetizioni = (TextView) row.findViewById(R.id.adapterReps);
        TextView minuti = (TextView) row.findViewById(R.id.adapterMinuti);
        TextView secondi = (TextView) row.findViewById(R.id.adapterSecondi);

        esercizio.setText(list.get(position).nome);
        serie.setText(String.valueOf(list.get(position).serie));
        ripetizioni.setText(String.valueOf(list.get(position).ripetizioni));
        minuti.setText(String.valueOf(list.get(position).minuti));
        secondi.setText(String.valueOf(list.get(position).secondi));
        return row;
    }


    private void setUpData(View view,int position)
    {
        LinearLayout layout=view.findViewById(R.id.layoutList);
        TextView esercizio = (TextView) view.findViewById(R.id.adapterEsercizio);
        TextView serie = (TextView) view.findViewById(R.id.adapterSerie);
        TextView ripetizioni = (TextView) view.findViewById(R.id.adapterReps);
        TextView minuti = (TextView) view.findViewById(R.id.adapterMinuti);
        TextView secondi = (TextView) view.findViewById(R.id.adapterSecondi);

        esercizio.setText(list.get(position).nome);
        serie.setText(list.get(position).serie);
        ripetizioni.setText(list.get(position).ripetizioni);
        minuti.setText(list.get(position).minuti);
        secondi.setText(list.get(position).secondi);

        /*layout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Toast.makeText(context,position,Toast.LENGTH_SHORT);
            }
        });*/
    }

}
