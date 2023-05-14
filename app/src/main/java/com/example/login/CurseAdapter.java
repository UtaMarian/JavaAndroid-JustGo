package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.login.data.Cursa;

import java.util.ArrayList;
import java.util.List;

public class CurseAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<Cursa> curse =new ArrayList<Cursa>();

    public CurseAdapter(Context ctx, ArrayList<Cursa> curse) {
        this.ctx = ctx;
        this.curse = curse;
    }

    @Override

    public int getCount() {
        return this.curse.size();
    }

    @Override
    public Object getItem(int position) {
        return this.curse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.item_list, parent,false);
        TextView departure=v.findViewById(R.id.item1);
        TextView arrival=v.findViewById(R.id.item3);
        TextView departureDate=v.findViewById(R.id.item2);
        TextView arrivalDate=v.findViewById(R.id.item4);
        TextView date=v.findViewById(R.id.itemCompanie);
        TextView locuri=v.findViewById(R.id.itemLocuri);
        TextView price=v.findViewById(R.id.priceitem);
        Cursa c=(Cursa) getItem(position);
        departure.setText(c.getDeparture());
        arrival.setText(c.getArrival());
        departureDate.setText(c.getDepartureDate().split("T")[1]);
        arrivalDate.setText(c.getArrivalDate().split("T")[1]);
        date.setText(c.getDepartureDate().split("T")[0]);
        locuri.setText(String.valueOf(c.getCapacity()));
        price.setText(String.valueOf(c.getPrice()));
        return v;
    }
}
