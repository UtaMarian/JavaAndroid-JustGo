package com.example.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.login.Model.User;
import com.example.login.R;
import com.example.login.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    User user=null;
    private GoogleMap mMap;
    private String type="circle";
    private List<LatLng> pins=new ArrayList<>();
    private ActivityMapsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            user= (User) bundle.getSerializable("user");
        }
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageView backBtn= (ImageView) findViewById(R.id.backArrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapsActivity.this,HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        LatLng bucharest = new LatLng(44.439663, 26.096306);
//        mMap.addMarker(new MarkerOptions().position(bucharest).title("Bucharest"));
//
//        LatLng cluj = new LatLng(46.770439, 23.591423);
//        mMap.addMarker(new MarkerOptions().position(cluj).title("Cluj"));
//
//        LatLng craiova = new LatLng(44.318179, 23.806620);
//        mMap.addMarker(new MarkerOptions().position(craiova).title("Craiova"));
//
//        LatLng timisoara = new LatLng(45.752771, 21.225642);
//        mMap.addMarker(new MarkerOptions().position(timisoara).title("Timisoara"));
//
//        LatLng iasi = new LatLng(47.160591, 27.587740);
//        mMap.addMarker(new MarkerOptions().position(iasi).title("Iasi"));
//
//        LatLng constanta = new LatLng(44.178545, 28.652116);
//        mMap.addMarker(new MarkerOptions().position(constanta).title("Constanta"));
//
//        LatLng sibiu = new LatLng(45.795118, 24.151685);
//        mMap.addMarker(new MarkerOptions().position(sibiu).title("Sibiu"));
//
//        LatLng brasov = new LatLng(45.657974, 25.601198);
//        mMap.addMarker(new MarkerOptions().position(brasov).title("Brasov"));
//
//        // Move the camera to Bucharest
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bucharest, 7));
//
//        PolylineOptions polylineOptions = new PolylineOptions()
//                .add(constanta)
//                .add(bucharest)
//                .add(craiova)
//                .add(timisoara)
//                .add(cluj)
//                .add(iasi)
//                .add(brasov)
//                .add(sibiu)
//                .add(constanta)
//                .color(Color.RED)
//                .width(5);
//
//        mMap.addPolyline(polylineOptions);
        mMap = googleMap;

        LatLng tgjiu = new LatLng(45.037785, 23.269428);
        if(type.equals("circle"))
            mMap.addCircle(new CircleOptions()
                    .center(tgjiu)
                    .radius(10000));
        else
            mMap.addPolygon((new PolygonOptions())
                    .add(new LatLng(tgjiu.latitude-0.05, tgjiu.longitude-0.1),
                            new LatLng(tgjiu.latitude+0.05, tgjiu.longitude-0.1),
                            new LatLng(tgjiu.latitude+0.05, tgjiu.longitude+0.1),
                            new LatLng(tgjiu.latitude-0.05, tgjiu.longitude+0.1)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tgjiu,12));

        PolygonOptions polygonOptions=new PolygonOptions();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng pin) {
                pins.add(pin);
                System.out.println("Punct nou: ("+pin.latitude+","+pin.longitude+")");
                mMap.addMarker(new MarkerOptions().position(pin));

                polygonOptions.add(pin);
                mMap.addPolygon(polygonOptions);

            }
        });
    }

}