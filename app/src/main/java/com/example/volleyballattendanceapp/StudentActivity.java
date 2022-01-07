package com.example.volleyballattendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String teamName;
    private String sportName;
    private int position;
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
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        calendar = new MyCalendar();
        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        teamName = intent.getStringExtra("teamName");
        sportName = intent.getStringExtra("sportName");
        position = intent.getIntExtra("position",-1);
        tid = intent.getLongExtra("tid",-1);

        setToolBar();
        loadData();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position->changedStatus(position));

    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(tid);
        Log.i("1234567890","loadData" +tid);
        studentItems.clear();
        while(cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));

            studentItems.add(new StudentItem(sid,roll,name));

        }
        cursor.close();
    }

    private void changedStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if(status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);

        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText(teamName);
        subtitle.setText(sportName);

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private boolean onMenuItemClick(MenuItem menuItem) {

        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        if(menuItem.getItemId()==R.id.show_Calendar){
            showCalendar();
        }

        return true;
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOkClickListener(this::OnCalendarClicked);
    }

    private void OnCalendarClicked(int year, int month, int day) {
        calendar.setDate(year,month,day);
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
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
        dbHelper.deleteClass(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}