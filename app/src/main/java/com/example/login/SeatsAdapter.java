package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SeatsAdapter  extends BaseAdapter {
    Context c;
    List<Integer> items = new ArrayList<>();
    List<Integer> reserved = new ArrayList<>();
    List<Integer> selected = new ArrayList<>();

    public SeatsAdapter(Context c, List<Integer> arr,List<Integer> sel,List<Integer> res) {
        this.c = c;
        items = arr;
        this.reserved=res;
        this.selected=sel;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(c);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.seat_card, viewGroup, false);
        ImageView imageView = v.findViewById(R.id.idSeatIcon);
        TextView textView = v.findViewById(R.id.idSeatNumber);
        textView.setText(this.getItem(i).toString());

        int seatNumber = i - 1;
        // Change the color of the ImageView for reserved seats
        if (reserved.contains(seatNumber)) {
            imageView.setImageResource(R.drawable.red_seat);
        }
        // Change the color of the ImageView for selected seats
        else if (selected.contains(seatNumber)) {
            imageView.setImageResource(R.drawable.yellow_seat);
        }
        // Use the default image for unreserved and unselected seats
        else {
            imageView.setImageResource(R.drawable.green_seat);
        }

        return v;
    }

}