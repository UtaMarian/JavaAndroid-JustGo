package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.login.Model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SearchRoutesActivity extends AppCompatActivity {

    private User user=null;
    EditText date;
    DatePickerDialog datePickerDialog;
    EditText departure;
    EditText arrival;
    Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_routes);


        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            user= (User) bundle.getSerializable("user");
        }

        departure=findViewById(R.id.plecare);
        arrival=findViewById(R.id.sosire);
        date = (EditText) findViewById(R.id.date);
        search_btn=findViewById(R.id.search_btn);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(SearchRoutesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchRoutesActivity.this,ListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("departure",departure.getText().toString());
                bundle.putString("arrival",arrival.getText().toString());
                bundle.putString("date",date.getText().toString());
                salveazaInFisier(date.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Button backBtn=findViewById(R.id.backBtnList2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(SearchRoutesActivity.this,HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

    }
    public void salveazaInFisier(String date) {
        try {
            FileOutputStream fos;
            fos = openFileOutput("data.txt", MODE_PRIVATE);
            fos.write(date.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}