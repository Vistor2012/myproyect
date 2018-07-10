package com.example.victor.myproyect;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener{

    private GoogleMap mMap;
    private GoogleMap googleMap;

    private UiSettings uiSettings;
    private ArrayList<LatLng> points;
    private boolean isInadd = false;
    private ArrayList<Marker> listMarkers;
    private Button enviar;
    private Button agregar;
    private Button remover;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listMarkers = new ArrayList<Marker>();
        loadData();
    }

    private void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        //Falta URL del Servicio:----------------------
        String url = "http://192.168.43.142:7777/api/v1.0/" + "inmuebles";

        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONArray aux = timeline;
                ArrayList<LatLng> list_coor = new ArrayList<LatLng>();
                for(int i=0;i < aux.length();i++){
                    try {
                        JSONObject obj = aux.getJSONObject(i);
                        double lat = Double.parseDouble(obj.get("lat").toString());
                        double lng = Double.parseDouble(obj.get("lng").toString());
                        list_coor.add(new LatLng(lat,lng));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i=0;i < list_coor.size();i++){
                    setMarker(list_coor.get(i));
                }

            }
        });
    }
    private void setMarker(LatLng latLng){
        Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(":D"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng Potosi = new LatLng(-19.5788458, -65.7586330);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Potosi,15));

        mMap.setOnMapClickListener(this);
        points = new ArrayList<>();
    }


    @Override
    public void onClick(View v) {

    }

    private void msjToasFail() {
        Toast.makeText(this,"Error al insertar", Toast.LENGTH_LONG).show();
    }

    private void msjToasSuccess() {
        Toast.makeText(this,"Felicidades", Toast.LENGTH_LONG).show();
    }

    private void msjtxt() {
        if(isInadd = true){
            Toast.makeText(this,"Agregando Marcador",Toast.LENGTH_LONG).show();
        }else{

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(isInadd){
            Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(":D"));
            listMarkers.add(m);
            Toast.makeText(this,latLng.toString(),Toast.LENGTH_SHORT).show();
        }
    }
}