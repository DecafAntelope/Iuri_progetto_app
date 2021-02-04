package com.Iuri.iuri_progetto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class DialogReport extends AppCompatDialogFragment {

    TextInputEditText motivo;
    String uidUtente;
    DialogReportListener listener;
    Button btnChiudi,btnInvia;

    public DialogReport(String u)
    {
        uidUtente=u;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DialogReportListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_report,null);
        //this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        motivo = (TextInputEditText) view.findViewById(R.id.motivoReport);
        btnChiudi = (Button) view.findViewById(R.id.RepChiudi);
        btnChiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogReport.this.dismiss();
            }
        });
        btnInvia = (Button) view.findViewById(R.id.RepInvia);
        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!motivo.getText().toString().equals(""))
                {
                    segnalazione s  = new segnalazione(motivo.getText().toString(),uidUtente);
                    listener.Report(s);
                    DialogReport.this.dismiss();
                }
            }
        });

        builder.setView(view)
                //.setTitle("SEGNALAZIONE")
                .setCancelable(false)
                /*.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Segnala", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!motivo.getText().toString().equals(""))
                        {
                            segnalazione s  = new segnalazione(motivo.getText().toString(),uidUtente);
                            listener.Report(s);
                        }
                    }
                })*/;

        return builder.create();
    }

    public interface DialogReportListener
    {
        void Report(segnalazione s);
    }
}
