package com.example.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.login.data.Cursa;
import com.example.login.data.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends BaseAdapter {

    private Context ctx;
    private List<Ticket> tickets =new ArrayList<Ticket>();

    public TicketAdapter(Context ctx, List<Ticket> tickets) {
        this.ctx = ctx;
        this.tickets = tickets;
    }

    @Override

    public int getCount() {
        return this.tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.ticket_list, parent,false);
        TextView price=v.findViewById(R.id.price_ticket);
        TextView type=v.findViewById(R.id.type_ticket);
        TextView date=v.findViewById(R.id.date_ticket);

        Ticket ticket=(Ticket) getItem(position);

        price.setText(String.valueOf(ticket.getPrice()));
        type.setText(ticket.getTicketType());
        date.setText(ticket.getDateSchedule().split("T")[0]);
        return v;
    }
}
