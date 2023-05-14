package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.Model.User;
import com.example.login.data.Ticket;
import com.example.login.database.TicketDatabase;
import com.example.login.service.RetrofitService;
import com.example.login.service.UserClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketsActivity extends AppCompatActivity {

    List<Ticket> tickets=new ArrayList<>();
    TicketDatabase db;
    TicketAdapter adapter;
    User user=null;
    ListView lv;
    Handler mainHandler;
    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onResume() {
        super.onResume();
         handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    adapter.notifyDataSetChanged();
                }
            }
        };

         mainHandler = new Handler(Looper.getMainLooper());

        Thread secondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Ticket> ticketsArray = (ArrayList<Ticket>) db.ticketDao().getAll();
                if (ticketsArray != null) {
                    tickets.addAll(ticketsArray);
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        });

        secondThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
             user=(User) bundle.getSerializable("user");
        }

        db = Room.databaseBuilder(getApplicationContext(), TicketDatabase.class, "tickets-db").build();
        adapter = new TicketAdapter(TicketsActivity.this, tickets);
        lv=findViewById(R.id.ticketsList);
        lv.setAdapter(adapter);



        Button refreshBtn= findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> formData = new HashMap<>();
                formData.put("id",user.getId());

                Call<ArrayList<Ticket>> call = RetrofitService.getClient().create(UserClient.class).getTickets(formData);
                call.enqueue(new Callback<ArrayList<Ticket>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Ticket>> call, @NonNull Response<ArrayList<Ticket>> response) {
                        assert response.body() != null;
                        System.out.println(response.body().get(0).getPrice());

                        tickets.removeAll(tickets);
                        tickets.addAll(response.body());

                        Thread thirdThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db.ticketDao().deleteAll();
                                for( Ticket d: tickets){
                                    db.ticketDao().insert(d);
                                }

                            }
                        });

                        thirdThread.start();

                        System.out.println(tickets.size());
                        handler.sendEmptyMessage(1);

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Ticket>> call, Throwable t) {
                        Toast.makeText(TicketsActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Button backBtn=findViewById(R.id.backBtnList);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(TicketsActivity.this,HomeActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("user",user);
                intent1.putExtras(intent1);
                startActivity(intent1);
            }
        });
    }
}