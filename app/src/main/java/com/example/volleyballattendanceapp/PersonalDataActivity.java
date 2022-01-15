package com.example.volleyballattendanceapp;

import static java.lang.Integer.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Calendar;

public class PersonalDataActivity extends AppCompatActivity {
    TextView tvPresent, tvLate, tvAbsent;
    PieChart pieChart;
    Toolbar toolbar;
    String month;
    StudentItem studentItem;
    DbHelper dbHelper;
    private final int PRESENT = 0;
    private final int LATE = 1;
    private final int ABSENT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        tvPresent = findViewById(R.id.tvPresent);
        tvLate = findViewById(R.id.tvLate);
        tvAbsent = findViewById(R.id.tvAbsent);
        pieChart = findViewById(R.id.piechart);
        setData();
        setToolBar();
    }
    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        String name = getIntent().getStringExtra("name");
        title.setText("Review of " + name);
        subtitle.setVisibility(View.GONE);

        back.setOnClickListener(v->onBackPressed());
    }
    private void setData() {
        int[] statusArray = getStudentStatusData();

        tvPresent.setText(Integer.toString(statusArray[PRESENT]));
        tvLate.setText(Integer.toString(statusArray[LATE]));
        tvAbsent.setText(Integer.toString(statusArray[ABSENT]));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Present",
                        Integer.parseInt(tvPresent.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Late",
                        Integer.parseInt(tvLate.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Absent",
                        Integer.parseInt(tvAbsent.getText().toString()),
                        Color.parseColor("#EF5350")));
    }
    private int[] getStudentStatusData(){
        DbHelper dbHelper = new DbHelper(this);
        month = getIntent().getStringExtra("month");
        ArrayList<Integer> datesWithTraining = getIntent().getIntegerArrayListExtra("datesWithTraining");
        long sid = getIntent().getLongExtra("sid",-1);
        int[] statusArray = {0,0,0};
        for(int i = 0;i< datesWithTraining.size();i++){
            String day = datesWithTraining.get(i).toString();
            if(day.length()==1)
                day = "0"+day;
            String date = day+ "." + month;
            String status = dbHelper.getStatus(sid,date);
            if(status.charAt(0) == 'P'){
                statusArray[PRESENT]++;
            }
            if(status.charAt(0) == 'L'){
                statusArray[LATE]++;
                Log.i("123","late " + statusArray[LATE]);
            }
            if(status.charAt(0) == 'A'){
                statusArray[ABSENT]++;
            }
        }
        return  statusArray;
    }
}