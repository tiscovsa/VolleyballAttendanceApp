package com.example.volleyballattendanceapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    public static final String TEAM_UPDATE_DIALOG = "updateTeam";
    public static final String STUDENT_ADD_DIALOG ="addStudent";
    public static final String STUDENT_UPDATE_DIALOG ="updateStudent";

    private OnClickListener listener;
    private int roll;
    private String name;
    private DbHelper dbHelper;

    public MyDialog(int roll, String name) {

        this.roll = roll;
        this.name = name;
    }

    public MyDialog(){

    }

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
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog = getAddStudentDialog();
        if(getTag().equals(TEAM_UPDATE_DIALOG))dialog = getUpdateTeamDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog = getUpdateStudentDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Player");


        EditText roll_edt = view.findViewById(R.id.est01);
        EditText name_edt = view.findViewById(R.id.edt02);

        roll_edt.setHint("Roll");
        name_edt.setHint("Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        roll_edt.setText(roll +"");
        roll_edt.setEnabled(false);
        name_edt.setText(name);

        cancel.setOnClickListener( v-> dismiss());
        add.setOnClickListener( v-> {
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            listener.onClick(roll,name);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Training");


        EditText team_edt = view.findViewById(R.id.est01);
        EditText sport_edt = view.findViewById(R.id.edt02);

        team_edt.setHint("Team Name");
        sport_edt.setHint("Sport Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener( v-> dismiss());
        add.setOnClickListener( v-> {
            String teamName = team_edt.getText().toString();
            String sportName = sport_edt.getText().toString();
            listener.onClick(teamName,sportName);
            dismiss();
        });
        return builder.create();

    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.student_dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Player");


        EditText name_edt = view.findViewById(R.id.est01);

        name_edt.setHint("Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener( v-> dismiss());
        add.setOnClickListener( v-> {
            String name = name_edt.getText().toString();
            name_edt.setText("");

            listener.onClick("",name);
        });
        return builder.create();
    }

    private Dialog getAddTeamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Training");


        EditText team_edt = view.findViewById(R.id.est01);
        EditText sport_edt = view.findViewById(R.id.edt02);

        team_edt.setHint("Training Name");
        sport_edt.setHint("Team Name");
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
