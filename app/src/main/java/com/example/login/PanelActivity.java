package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.login.Model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;


public class PanelActivity extends AppCompatActivity {

    User user=null;
    private PieChart mPieChart;
    private BarChart mBarChart;

    private Button mPieChartButton;
    private Button mBarChartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);


        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            user= (User) bundle.getSerializable("user");
        }
        ImageView backBtn=findViewById(R.id.backArrowPanel);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PanelActivity.this,HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mPieChart = findViewById(R.id.pie_chart);
        mBarChart = findViewById(R.id.bar_chart);

        mPieChartButton = findViewById(R.id.pie_chart_button);
        mBarChartButton = findViewById(R.id.bar_chart_button);


        setupPieChart();
        setupBarChart();


        mPieChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPieChart.setVisibility(View.VISIBLE);
                mBarChart.setVisibility(View.GONE);
            }
        });
        mBarChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPieChart.setVisibility(View.GONE);
                mBarChart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setTransparentCircleRadius(61f);
        mPieChart.setCenterText("Curse fecvente");
        mPieChart.setCenterTextSize(20f);
        mPieChart.setCenterTextColor(Color.BLACK);
        mPieChart.setHoleRadius(58f);
        mPieChart.setDrawCenterText(true);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25, "Buc-Cluj"));
        entries.add(new PieEntry(15, "Cra-Buc"));
        entries.add(new PieEntry(10, "Cluj-Tim"));
        entries.add(new PieEntry(5, "Tim-Cluj"));
        entries.add(new PieEntry(45, "Iasi-Buc"));

        PieDataSet dataSet = new PieDataSet(entries, "Curse fecvente");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.MAGENTA);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        mPieChart.setData(data);
        mPieChart.invalidate();
    }
    private void setupBarChart() {
        mBarChart.getDescription().setEnabled(true);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisRight().setDrawGridLines(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 50));
        entries.add(new BarEntry(2, 70));
        entries.add(new BarEntry(3, 30));
        entries.add(new BarEntry(4, 80));
        entries.add(new BarEntry(5, 60));

        BarDataSet dataSet = new BarDataSet(entries, "Curse");
        dataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        mBarChart.setData(data);
        mBarChart.invalidate();
    }

}