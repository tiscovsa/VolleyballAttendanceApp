package com.example.volleyballattendanceapp;

import static java.lang.Integer.valueOf;

import android.content.Context;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    ArrayList<SheetItem> sheetItems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ListAdapter(Context context, ArrayList<SheetItem> sheetItems) {
        this.sheetItems = sheetItems;
        this.context = context;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView date;
        public ListViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            date = itemView.findViewById(R.id.month);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sheet_item,parent,false);
        return new ListViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        String year = sheetItems.get(position).getDate().substring(3);
        String month = sheetItems.get(position).getDate().substring(0,2);
        String monthAndYear = getMonth(month) + " " + year;
        holder.date.setText(monthAndYear);

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

    @Override
    public int getItemCount() {
        return sheetItems.size();
    }
}