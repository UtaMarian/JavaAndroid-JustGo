package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.data.Cursa;
import com.example.login.data.Ticket;
import com.example.login.database.TicketDatabase;
import com.example.login.service.RetrofitService;
import com.example.login.service.UserClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyTicketActivity extends AppCompatActivity implements SeatListAdapter.OnSeatSelectedListener{

    private RecyclerView seatListRecyclerView;
    private SeatListAdapter seatListAdapter;
    private List<Integer> reservedSeats = new ArrayList<>();
    private List<Integer> availableSeats = new ArrayList<>();
    private List<Integer> selectedSeats = new ArrayList<>();
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        Cursa cursa=(Cursa) bundle.getSerializable("cursa");

        TextView departure=findViewById(R.id.departureCity);
        TextView arrival=findViewById(R.id.arrivalCity);
        TextView price=findViewById(R.id.priceRoute);
        departure.setText(cursa.getDeparture());
        arrival.setText(cursa.getArrival());
        price.setText(String.valueOf(cursa.getPrice()));

        TicketDatabase db = Room.databaseBuilder(getApplicationContext(), TicketDatabase.class, "tickets-db").build();

        String outDate="";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("dd/M/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = inputFormat.parse(citesteDinFisier());
            assert date != null;
            outDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        Map<String, String> formData = new HashMap<>();
        formData.put("Id", cursa.getId().toString());
        formData.put("DepartureDay", outDate);
        System.out.println(outDate);

        Call<List<Integer>> call = RetrofitService.getClient().create(UserClient.class).getReservationsSeats(formData);
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                assert response.body() != null;
                reservedSeats.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Log.d("API_FAILURE","Error on get seats number reservations");
            }
        });

        ImageView backBtn=findViewById(R.id.backArrowBuyTicket);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BuyTicketActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        // Populate the available seats list
        for (int i = 1; i <= cursa.getCapacity(); i++) {
            if (!reservedSeats.contains(i)) {
                availableSeats.add(i);
            }
        }

        GridView gridView = findViewById(R.id.seatGridList);

        SeatsAdapter baseAdapter = new SeatsAdapter((Context) this, availableSeats,selectedSeats,reservedSeats);
        gridView.setAdapter(baseAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int seatNumber = position - 1;
                if(selectedSeats.contains(seatNumber)){
                    selectedSeats.remove(Integer.valueOf(seatNumber));

                }
                else {
                    selectedSeats.add(seatNumber);
                }
                baseAdapter.notifyDataSetChanged();
            }
        });

        List<String> ticketTypes=new ArrayList<>();
        for(Integer v :selectedSeats) {
            ticketTypes.add("Adult");
        }
        Button buyBtn=findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Thread secondThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<selectedSeats.size();i++){
                            ArrayList<Ticket> ticketsArray=new ArrayList<>();
                            Ticket ticket=new Ticket();
                            ticket.setBusId(cursa.getBusId());
                            ticket.setTicketType(ticketTypes.get(i));
                            ticket.setPrice((int) cursa.getPrice());
                            ticket.setDateSchedule(date.toString());
                            ticket.setRouteId(cursa.getId());
                            ticket.setSeatNumber(selectedSeats.get(i));
                            ticket.setUserId("me");
                            db.ticketDao().insert(ticket);
                            db.close();
                        }
                    }
                });

                secondThread.start();
                reservedSeats.addAll(selectedSeats);
                selectedSeats.removeAll(selectedSeats);
                baseAdapter.notifyDataSetChanged();
            }
        });

    }
    @Override
    public void onSeatSelected(int seatNumber) {
        // Handle seat selection event
        Toast.makeText(this, "Seat " + seatNumber + " selected", Toast.LENGTH_SHORT).show();
    }

    public String citesteDinFisier() {
        try {
            FileInputStream fis = openFileInput("data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String mesaj = reader.readLine();

            fis.close();
            return mesaj;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}


