package com.rakeshkr.passwordsafe.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.R;


public class AlertDialogBuilder {
    AlertDialog.Builder alertDialogBuilder;
    Context my_context;
    public AlertDialogBuilder(Context context){
        my_context=context;
        alertDialogBuilder=new AlertDialog.Builder(context);
    }

    public void createDialog(String title,String msg){
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(my_context,"yes",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(my_context,"No",Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });

        alertDialogBuilder.create();
        alertDialogBuilder.show();





    }
}
