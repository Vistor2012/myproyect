package com.example.victor.myproyect;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class latiylongi_Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Context root;
    private MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = this;
        setContentView(R.layout.activity_latiylongi__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActive(toolbar);
        final MapView map = this.findViewById(R.id.mapView);
        if(map!=null){
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marker!=null){
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("lat",marker.getPosition().latitude);
                    params.put("lon",marker.getPosition().longitude);
                    String url = "http://192.168.1.7:7777/api/v1.0/" + "inmuebles";
                    client.patch(url, params,new JsonHttpResponseHandler());
                    Toast.makeText(root,"exitoso",Toast.LENGTH_LONG).show();
                }
            }
        });
        FloatingActionButton fab1= (FloatingActionButton)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(root,MainActivity.class);
                root.startActivity(out);
            }
        });

    }

    private void setSupportActive(Toolbar toolbar) {
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
        LatLng Potosi = new LatLng(-19.578297, -65.758633);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Potosi,14));
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this,latLng.toString(),Toast.LENGTH_SHORT).show();
        mMap.clear();
        marker = new MarkerOptions();
        marker.position(latLng);
        marker.title("Home");
        marker.draggable(true);
        mMap.addMarker(marker);
    }
}
