package com.example.volleyballattendanceapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment{
    public static final String TEAM_ADD_DIALOG = "addTeam";

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(String text1,String text2);

    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if(getTag().equals(TEAM_ADD_DIALOG))dialog=getAddTeamDialog();
        return dialog;
    }

    private Dialog getAddTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Team");


        EditText team_edt = view.findViewById(R.id.est01);
        EditText sport_edt = view.findViewById(R.id.edt02);

        team_edt.setHint("Team Name");
        sport_edt.setHint("Sport Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener( v-> dismiss());
        add.setOnClickListener( v-> {
            String teamName = team_edt.getText().toString();
            String sportName = sport_edt.getText().toString();
           listener.onClick(teamName,sportName);
            dismiss();
        });
        return builder.create();
    }
}
