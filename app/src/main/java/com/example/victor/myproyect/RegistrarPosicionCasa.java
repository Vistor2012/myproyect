package com.example.victor.myproyect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.victor.myproyect.DATA.DataApp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistrarPosicionCasa extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private Button guardar;
    private Intent intent;
    private Marker marcadorCasa;
    private Context root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_posicion_casa);
        root = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        guardar = this.findViewById(R.id.guardar);
        guardar.setOnClickListener(this);
        intent = getIntent();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Potosi = new LatLng(-19.5788458, -65.7586330);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Potosi,15));
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        marcadorCasa = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicacion de la casa"));
    }

    @Override
    public void onClick(View v) {
        String idCasa = intent.getExtras().getString("id");
        if (marcadorCasa != null) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("latitud",marcadorCasa.getPosition().latitude);
            params.put("longitud", marcadorCasa.getPosition().longitude);
            client.patch(DataApp.HOST_INMUEBLE+idCasa, params, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    String msn = null;
                    try {
                        msn = response.getString("msn");

                        if (msn!=null){
                            Intent formu = new Intent(root, MainActivity.class);
                            root.startActivity(formu);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }


            });

        }
    }
}
