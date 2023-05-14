package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.Model.User;
import com.example.login.data.Cursa;
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

public class ListActivity extends AppCompatActivity {

    ArrayList<Cursa> curse=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String departure=bundle.getString("departure");
        String arrival=bundle.getString("arrival");
        String date=bundle.getString("date");

        
        Map<String, String> formData = new HashMap<>();
        formData.put("Departure", departure);
        formData.put("Arrival", arrival);
        formData.put("Date", date);

        CurseAdapter adapter=new CurseAdapter(ListActivity.this,curse);
        ListView lv=findViewById(R.id.routeList);
        lv.setAdapter(adapter);
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        };
        Call<ArrayList<Cursa>> call = RetrofitService.getClient().create(UserClient.class).getRoutes(formData);
        call.enqueue(new Callback<ArrayList<Cursa>> () {
            @Override
            public void onResponse(Call<ArrayList<Cursa>>  call, @NonNull Response<ArrayList<Cursa>>  response) {
                assert response.body() != null;
                curse.addAll(response.body());
                System.out.println(curse.get(0).getArrival());
                handler.sendEmptyMessage(0);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Cursa","Id: "+curse.get(position).getId().toString());

                        Intent intent =new Intent(ListActivity.this,BuyTicketActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("cursa",curse.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<Cursa>>  call, Throwable t) {
                Toast.makeText(ListActivity.this, "failure", Toast.LENGTH_SHORT).show();
                System.out.println(t.toString());
            }
        });


        ImageView backBtn=findViewById(R.id.backArrowList);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }


}
