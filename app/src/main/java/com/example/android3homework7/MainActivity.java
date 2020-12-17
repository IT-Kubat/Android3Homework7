package com.example.android3homework7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android3homework7.preferences.Pref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<LatLng> savedPlace = new ArrayList<>();
    boolean location = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControlViews();
        Pref.init(this);
        if (savedPlace != null && getSharedPreferences(Pref.PREFERENCE_NAME, Context.MODE_PRIVATE) != null){
            savedPlace = Pref.getLocation(savedPlace);
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (savedInstanceState != null && getSharedPreferences(Pref.PREFERENCE_NAME, Context.MODE_PRIVATE) != null){
            addPoligonOptions();
        }
    }

    private void addPoligonOptions() {
        if (savedPlace != null){
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.strokeWidth(15f);
            polygonOptions.strokeColor(Color.GREEN);
            for (LatLng latLng: savedPlace) {
                polygonOptions.add(latLng);
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
            }
            mMap.addPolygon(polygonOptions);

        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        checkMyLocationPermission();

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            return;

        }

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(42.8307539, 74.5959216))
                .zoom(13f)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setMyLocationEnabled(true);
    }

    private static final int PERMISSION_REQUEST_CODE = 1;

    private void checkMyLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE)
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    mMap.setMyLocationEnabled(true);
                }
    }

    private void initControlViews() {
        Button btnMapNormal = findViewById(R.id.btn_normal);
        Button btnMapHybrid = findViewById(R.id.btn_hybrid);
        Button btnMapPolyline = findViewById(R.id.btn_polyline);
        Button btnMapPolygon = findViewById(R.id.btn_polygon);

        btnMapNormal.setOnClickListener(v -> setMapNormal());
        btnMapHybrid.setOnClickListener(v -> setMapHybrid());
        btnMapPolyline.setOnClickListener(v -> setPolyline());
        btnMapPolygon.setOnClickListener(v -> setPolygon());
    }

    private void setPolygon() {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(coords);
        mMap.addPolygon(polygonOptions);
    }

    private void setPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(coords);
        polylineOptions.color(Color.RED);
        polylineOptions.width(15.0f);
        mMap.addPolyline(polylineOptions);
    }

    private void setMapHybrid() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setMapNormal() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    List<LatLng> coords = new ArrayList<>();

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Hello, Geektech");
        mMap.addMarker(markerOptions);


        coords.add(latLng);
        Toast.makeText(this, "You clicked", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(this, "Long click", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        savedPlace.remove(marker.getPosition());

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void onClickHybrid(View view) {
        if (!location) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            location = true;
        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            location = false;
        }

    }

    public void onClickPolygonDel(View view) {
        addPoligonOptions();
        Pref.clear();
        Toast.makeText(this, "You deleted", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "onClickPolygonDel: " + savedPlace  );
    }

    public void onClickPolygon(View view) {
        addPoligonOptions();
        Pref.saveLocation(savedPlace);
        Toast.makeText(this, "You saved", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "onClickPolygon: " + savedPlace );
    }
}

