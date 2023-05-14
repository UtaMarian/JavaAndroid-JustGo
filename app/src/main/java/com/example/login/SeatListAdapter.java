package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class SeatListAdapter extends RecyclerView.Adapter<SeatListAdapter.ViewHolder> {

        private List<Integer> reservedSeats;
        private List<Integer> selectedSeats=new ArrayList<>();
        private List<Integer> availableSeats;
        private OnSeatSelectedListener listener;


        public SeatListAdapter(List<Integer> reservedSeats, List<Integer> availableSeats, OnSeatSelectedListener listener) {
            this.reservedSeats = reservedSeats;
            this.availableSeats = availableSeats;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int seatNumber = availableSeats.get(position);
            holder.seatNumberTextView.setText(String.valueOf(seatNumber));

            if (reservedSeats.contains(seatNumber)) {
                holder.seatImageView.setImageResource(R.drawable.red_seat);
                holder.itemView.setEnabled(false);
            } else {
                holder.seatImageView.setImageResource(R.drawable.green_seat);
                holder.itemView.setEnabled(true);
            }
            holder.itemView.setOnClickListener(v -> {
                if (holder.itemView.isEnabled()) {
                    if(!selectedSeats.contains(seatNumber)) {
                        holder.seatImageView.setImageResource(R.drawable.yellow_seat);
                        selectedSeats.add(seatNumber);
                        listener.onSeatSelected(seatNumber);
                    }else{
                        holder.seatImageView.setImageResource(R.drawable.green_seat);
                        selectedSeats.remove((Object)seatNumber);
                        //listener.onSeatSelected(seatNumber);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return availableSeats.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView seatImageView;
            public TextView seatNumberTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                seatImageView = itemView.findViewById(R.id.seat_image);
                seatNumberTextView = itemView.findViewById(R.id.seat_number);
            }
        }

        public interface OnSeatSelectedListener {
            void onSeatSelected(int seatNumber);
        }


}
