package com.example.volleyballattendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SheetItem> listItems = new ArrayList();
    private long tid;
    private int position;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        Intent intent = getIntent();
        recyclerView = findViewById(R.id.sheet_recycler);

        tid= getIntent().getLongExtra("tid",-1);
        Log.i("1234567890","onCreate: " +tid);
        loadListItems();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        position = intent.getIntExtra("position",-1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(this,listItems);
        recyclerView.setAdapter(adapter);
        setToolBar();

        adapter.setOnItemClickListener(position->openActivitySheetPosition(position));
    }
    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Months");

        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(v->onBackPressed());
    }
    private void openActivitySheetPosition(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String month = listItems.get(position).getDate();

        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",month);
        Log.i("1234567890","the month " +listItems.get(position).getDate());

        startActivity(intent);

    }

    private void loadListItems() {
        Cursor cursor = new DbHelper(this).getDistinctMonths(tid);

        while(cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));
            String subDate = date.substring(3);
            SheetItem sheetItem = new SheetItem(subDate);
            listItems.add(sheetItem);
        }
    }
}