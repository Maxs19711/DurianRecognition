package com.example.cardview;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;


public class MyDialogFragment extends DialogFragment {

    List<String> option;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        option = new ArrayList<>();
        option.add("select from gallery");
        option.add("Take a picture");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("select");
        builder.setItems(option.toArray(new String[0]), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(option.get(which).equals("select from gallery"))
                {
                    Intent myIntent = new Intent(getContext(), PredictPage.class);
                    startActivity(myIntent);
                }
                else if(option.get(which).equals("Take a picture"))
                {
                    try{
                        Intent myIntent2 = new Intent(getContext(), PredictCam.class);
                        startActivity(myIntent2);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(),"Error" + e, Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        return builder.create();
    }
}
