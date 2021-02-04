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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class DialogProgramma extends AppCompatDialogFragment {
    TextInputEditText nomeProgramma;
    dialogProgrammaListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialogProgrammaListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"devi implementare dialogProgramaListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.salvafile_dialog,null);
        nomeProgramma = (TextInputEditText) view.findViewById(R.id.txtDialogFile);
        builder.setView(view)
                .setTitle("Inserire nome Programma")
                .setCancelable(false)
                .setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("salva",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String programma = nomeProgramma.getText().toString().replaceAll(" ","");
                        if(!programma.equals(""))
                        {
                            if(!programma.contains(".txt") && !programma.contains(File.separator) && !programma.contains("."))
                            {
                                listener.applyTexts(programma);
                            }
                            else
                            {
                                Toast.makeText(view.getContext(), "Hai usato caratteri che non si possono utilizzare!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(view.getContext(), "Devi inserire qualcosa!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
         return builder.create();
    }

    public interface dialogProgrammaListener{
        void applyTexts(String programma);
    }
}
