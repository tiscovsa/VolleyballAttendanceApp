package com.example.volleyballattendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TeamItem> teamItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v-> showDialog());

        loadData();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new ClassAdapter(this,teamItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolBar();


    }

    private void loadData() {
        Cursor cursor = dbHelper.getTeamTable();

        teamItems.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.T_ID));
            String teamName = cursor.getString(cursor.getColumnIndex(DbHelper.TEAM_NAME_KEY));
            String sportName = cursor.getString(cursor.getColumnIndex(DbHelper.SPORT_NAME_KEY));

            teamItems.add(new TeamItem(id,teamName,sportName));

        }
    }

    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);

        title.setText("Attendance App");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("teamName",teamItems.get(position).getTeamName());
        intent.putExtra("sportName",teamItems.get(position).getSportName());
        intent.putExtra("position",position);
        intent.putExtra("tid",teamItems.get(position).getTid());
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.TEAM_ADD_DIALOG);
        dialog.setListener((teamName,sportName)->addTeam(teamName,sportName));
    }

    private void addTeam(String teamName,String sportName){
        long tid = dbHelper.addTeam(teamName,sportName);
        TeamItem teamItem =  new TeamItem( teamName,sportName);
        teamItems.add(teamItem);

        classAdapter.notifyDataSetChanged();
        this.recreate();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteTeam(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog(teamItems.get(position).getTeamName(),teamItems.get(position).getSportName());
        dialog.show(getSupportFragmentManager(),MyDialog.TEAM_UPDATE_DIALOG);
        dialog.setListener((teamName,sportName)->updateTeam(position,teamName,sportName));

    }

    private void updateTeam(int position, String teamName, String sportName) {
        dbHelper.updateTeam(teamItems.get(position).getTid(),teamName,sportName);
        teamItems.get(position).setTeamName(teamName);
        teamItems.get(position).setSportName(sportName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteTeam(int position) {
        dbHelper.deleteClass(teamItems.get(position).getTid());
        teamItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }
}