package com.example.volleyballattendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;


public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String teamName;
    private String sportName;
    private int position;
    ExtendedFloatingActionButton save;
    ExtendedFloatingActionButton discard;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DbHelper dbHelper;
    private long tid;
    private MyCalendar calendar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        calendar = new MyCalendar();
        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        teamName = intent.getStringExtra("teamName");
        sportName = intent.getStringExtra("sportName");
        position = intent.getIntExtra("position",-1);
        tid = intent.getLongExtra("tid",-1);

        setToolBar();
        loadData();

        save = findViewById(R.id.save);
        discard = findViewById(R.id.discard_btn);
        save.setVisibility(View.INVISIBLE);
        discard.setVisibility(View.INVISIBLE);
        save.setOnClickListener(v-> saveStatus());
        discard.setOnClickListener(v-> discardStatus());

        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position->changedStatus(position));
        loadStatusData();
    }

    private void discardStatus() {
        this.recreate();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(tid);
        studentItems.clear();
        while(cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));

            studentItems.add(new StudentItem(sid,roll,name));

        }
        cursor.close();
    }
    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);

        ImageButton back = toolbar.findViewById(R.id.back);

        title.setText(teamName);
        subtitle.setText(sportName+" | "+calendar.getDate());

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void changedStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if(status.equals("P")) status = "A";
        else if(status.equals("A")) status = "L";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
        save.setVisibility(View.VISIBLE);
        discard.setVisibility(View.VISIBLE);
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems) {
            if(dbHelper.getStatus(studentItem.getSid(),calendar.getDate()) == null){
                String status = studentItem.getStatus();
                if (status == "") status = "A";
                long value = dbHelper.addStatus(studentItem.getSid(), tid, calendar.getDate(), status);

                if (value == 1) {
                    dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);
                }
            }
            else{
                String status = studentItem.getStatus();
                if (status == "") status = "A";
                dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);
            }

        }
        save.setVisibility(View.INVISIBLE);
        discard.setVisibility(View.INVISIBLE);
    }

    private void loadStatusData(){
        for(StudentItem studentItem : studentItems){
            String status = dbHelper.getStatus(studentItem.getSid(),calendar.getDate());
            if(status!=null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }
    private void clearStatus(){
        for(int i = 0;i<studentItems.size();i++){
            StudentItem studentItem = studentItems.get(i);
            dbHelper.deleteStatus(studentItem,calendar.getDate());
        }
        this.recreate();
    }
    private boolean onMenuItemClick(MenuItem menuItem) {

        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        if(menuItem.getItemId()==R.id.show_Calendar){
            showCalendar();
        }
        if(menuItem.getItemId()==R.id.show_attendance_sheet){
            openSheetList();
        }
        if(menuItem.getItemId()==R.id.clear){
            clearStatus();
        }

        return true;
    }

    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];

        for(int i = 0;i<idArray.length;i++)
            idArray[i] = studentItems.get(i).getSid();

        for(int i = 0;i<rollArray.length;i++)
            rollArray[i] = studentItems.get(i).getRoll();

        for(int i = 0;i<nameArray.length;i++)
            nameArray[i] = studentItems.get(i).getName();

        Intent intent = new Intent(this,SheetListActivity.class);
        intent.putExtra("tid",tid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOkClickListener(this::OnCalendarClicked);
    }

    private void OnCalendarClicked(int year, int month, int day) {
        calendar.setDate(year,month,day);
        subtitle.setText(sportName+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(name));
    }

    private void addStudent(String name) {
        if(name.length() == 0){
            name = "The nameless one";
        }
        int roll = studentItems.size()+1;
        long sid = dbHelper.addStudent(tid,roll,name);
        StudentItem studentItem = new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position,String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        int rollDeleted = studentItems.get(position).getRoll();
        studentItems.remove(position);
        for(int i = 0;i< studentItems.size();i++){
            if(studentItems.get(i).getRoll()>rollDeleted){
                studentItems.get(i).setRoll(studentItems.get(i).getRoll()-1);
                updateStudentRoll(studentItems.get(i));
            }
        }
        this.recreate();
        adapter.notifyItemRemoved(position);
    }

    private void updateStudentRoll(StudentItem studentItem) {
        dbHelper.updateStudentRoll(studentItem.getSid(),studentItem.getRoll());
    }
}