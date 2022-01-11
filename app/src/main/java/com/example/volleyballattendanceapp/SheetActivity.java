package com.example.volleyballattendanceapp;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {
    Toolbar toolbar;
    String month;
    TableLayout tableLayout;
    long[] idArray;
    long sid;
    String name;
    ArrayList <Integer> datesWithTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        setToolBar();
        showTable();

    }

    private void setToolBar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        month = getIntent().getStringExtra("month");
        String monthString = getMonth(month.substring(0,2));

        title.setText("Review of " + monthString);
        subtitle.setVisibility(View.GONE);

        back.setOnClickListener(v->onBackPressed());
    }

    private String getMonth(String month) {
        String monthString;
        int numberMonth = Integer.parseInt(month);

        switch (numberMonth) {
            case 1:  monthString = "January";
                break;
            case 2:  monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    private void showTable() {
        DbHelper dbHelper = new DbHelper(this);
        tableLayout = findViewById(R.id.tableLayout);
        idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        ArrayList<Integer> daysWithTraining= new ArrayList<>();
        month = getIntent().getStringExtra("month");
        int DAY_IN_MONTH = getDayInMonth(month);

        //row setup
        int rowSize = idArray.length + 1;

        TableRow[] rows = new TableRow[rowSize];
        TextView[] roll_tvs = new TextView[rowSize];
        TextView[] name_tvs = new TextView[rowSize];
        TextView[][] status_tvs = new TextView[rowSize][DAY_IN_MONTH+1];
        roll_tvs[0] = new TextView(this);
        name_tvs[0] = new TextView(this);
        for(int i = 1;i<=DAY_IN_MONTH;i++){
            status_tvs[0][i] = new TextView(this);
        }

        for(int i = 1; i < rowSize; i++){
            roll_tvs[i] = new TextView(this);
            name_tvs[i] = new TextView(this);
            for(int j = 1; j <= DAY_IN_MONTH; j++){
                String day = String.valueOf(j);
                if(day.length()==1) day = "0"+day;
                String date = day+"."+month;
                String status = dbHelper.getStatus(idArray[i-1],date);
                if(status != null) {
                    daysWithTraining.add(j);
                    status_tvs[i][j] = new TextView(this);
                }
            }
        }

        //header
        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTextColor(Color.parseColor("#000000"));
        roll_tvs[0].setTypeface(roll_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTextColor(Color.parseColor("#000000"));
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        int dateTraining = 0;
        datesWithTraining = removeDuplicates(daysWithTraining);

        for(int i = 1; dateTraining != datesWithTraining.size(); i++){
            if(i == datesWithTraining.get(dateTraining)){
                status_tvs[0][i].setText(String.valueOf(i));
                status_tvs[0][i].setTextColor(Color.parseColor("#000000"));
                status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
                dateTraining++;
            }

        }

        for(int i = 1; i < rowSize; i++){
            roll_tvs[i].setText(String.valueOf(rollArray[i-1]));
            roll_tvs[i].setTextColor(Color.parseColor("#000000"));
            name_tvs[i].setText(nameArray[i-1]);
            name_tvs[i].setTextColor(Color.parseColor("#000000"));
            for(int j = 0; j < datesWithTraining.size(); j++){
                String day = String.valueOf(datesWithTraining.get(j));
                if(day.length()==1) day = "0"+day;
                String date = day+"."+month;
                String status = dbHelper.getStatus(idArray[i-1],date);
                status_tvs[i][datesWithTraining.get(j)].setText(status);
            }

        }
        //filling
        for(int i = 0; i < rowSize; i++){
            rows[i] = new TableRow(this);

            if(i % 2 == 0)
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
           else
                rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);
            rows[i].setId(i);
            rows[i].setClickable(true);
            rows[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = Integer.valueOf(v.getId())-1;
                    sid = idArray[position];
                    name = name_tvs[v.getId()].getText().toString();
                    openPersonalDataActivity(sid,name);
                }
            });
            for(int j = 0; j < datesWithTraining.size(); j++){

                status_tvs[i][datesWithTraining.get(j)].setPadding(16, 16, 16, 16);

                if (status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) == 'A') {
                    status_tvs[i][datesWithTraining.get(j)].setTextColor(Color.parseColor("#FF0000"));
                }
                if (status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) == 'P') {
                    status_tvs[i][datesWithTraining.get(j)].setTextColor(Color.parseColor("#006400"));
                }
                if (status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) == 'L') {
                    status_tvs[i][datesWithTraining.get(j)].setTextColor(Color.parseColor("#FFD801"));
                }
                if (status_tvs[i][datesWithTraining.get(j)].getText().toString().length() == 1 && status_tvs[i][datesWithTraining.get(j)].getText().toString().length() == 1 && status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'P' &&
                        status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'A' && status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'L') {
                    String correctDate = "0" + status_tvs[i][datesWithTraining.get(j)].getText();
                    status_tvs[i][datesWithTraining.get(j)].setText(correctDate);
                }
                if(status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'P' && status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'A' &&
                        status_tvs[i][datesWithTraining.get(j)].getText().charAt(0) != 'L'){
                    String attachMonth = status_tvs[i][datesWithTraining.get(j)].getText() +"."+getMonthNumber(month);
                    status_tvs[i][datesWithTraining.get(j)].setText(attachMonth);
                }

                rows[i].addView(status_tvs[i][datesWithTraining.get(j)]);

                }

            tableLayout.addView(rows[i]);

            }

        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);

        }

    private void openPersonalDataActivity(long sid,String name) {
        Intent intent = new Intent(this,PersonalDataActivity.class);
        intent.putExtra("month",month);
        intent.putExtra("datesWithTraining",datesWithTraining);
        intent.putExtra("sid",sid);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    private String getMonthNumber(String month) {
        String monthIndex = month.substring(0,2);
        return  monthIndex;
    }


    private int getDayInMonth(String month) {
        int monthIndex = valueOf(month.substring(0,1));
        int year =  valueOf(month.substring(4));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

}