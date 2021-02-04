package com.Iuri.iuri_progetto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class DialogModifica extends AppCompatDialogFragment {

    TextInputEditText nomeEs,serie,ripetizioni,minuti,secondi;
    dialogModificaListener listener;
    Esercizio esercizio;
    public  DialogModifica(Esercizio e)
    {
        esercizio = e;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialogModificaListener) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+"devi implementare dialogProgramaListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.modifica_es_dialog,null);
        nomeEs = (TextInputEditText) view.findViewById(R.id.dialNome);
        serie = (TextInputEditText) view.findViewById(R.id.dialSerie);
        ripetizioni = (TextInputEditText) view.findViewById(R.id.dialReps);
        minuti = (TextInputEditText) view.findViewById(R.id.dialMin);
        secondi = (TextInputEditText) view.findViewById(R.id.dialSec);

        nomeEs.setText(esercizio.nome);
        serie.setText(String.valueOf(esercizio.serie));
        ripetizioni.setText(String.valueOf(esercizio.ripetizioni));
        minuti.setText(String.valueOf(esercizio.minuti));
        secondi.setText(String.valueOf(esercizio.secondi));

        builder.setView(view)
                .setTitle("Modifica Esercizio")
                .setCancelable(false)
                .setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String es = nomeEs.getText().toString(),se = serie.getText().toString(),rip = ripetizioni.getText().toString(),min = minuti.getText().toString(),sec = secondi.getText().toString();
                        if(!es.equals("") && !se.equals("") && !rip.equals("") && !min.equals("") && !sec.equals(""))
                        {
                            Esercizio e = new Esercizio(es,Integer.parseInt(min),Integer.parseInt(sec),Integer.parseInt(rip),Integer.parseInt(se));
                            listener.applicaTestoModifica(e);
                        }
                    }
                });
        return builder.create();
    }

    public interface dialogModificaListener{
        void applicaTestoModifica(Esercizio e);
    }
}
