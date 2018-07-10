package com.example.victor.myproyect;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.victor.myproyect.ListDataSource.CustomAdapter;
import com.example.victor.myproyect.ListDataSource.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListActivity extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        root = this;

        LISTINFO = new ArrayList<ItemList>();
        LIST =(ListView) this.findViewById(R.id.list_house);

        loadInitialRestData();
    }

    private void loadInitialRestData() {
        AsyncHttpClient client = new AsyncHttpClient();
        // Toast.makeText(getApplicationContext(),"esta entrando aqui",Toast.LENGTH_SHORT).show();
        //aqui donde tiene q cargar la informacion
        String url = "http://192.168.43.142:7777/api/v1.0/inmuebles";
        //String descripcion="";
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Toast.makeText(getApplicationContext(),"entra",Toast.LENGTH_SHORT).show();

                try {
                    for(int i = 0; i < response.length(); i++) {
                        Toast.makeText(getApplicationContext(),"esta entrando aqui",Toast.LENGTH_SHORT).show();
                        JSONObject itemJson = response.getJSONObject(i);
                        Toast.makeText(getApplicationContext(),itemJson+"",Toast.LENGTH_SHORT).show();
                        String descripcion_p = itemJson.getString("descripcion");
                        String servicios_p = itemJson.getString("servicios");
                        String precio_p = itemJson.getString("precio");
                        String superficie_p = itemJson.getString("superficie");
                        String tipo_operacion = itemJson.getString("tipo_operacion");
                        String direccion_p = itemJson.getString("direccion");
                        //String imdbID = itemJson.getString("_id");
                        //String images = itemJson.getString("image_casa");

                        ItemList item = new ItemList(descripcion_p, servicios_p, precio_p, superficie_p, direccion_p, tipo_operacion);
                        LISTINFO.add(item);
                    }

                    ADAPTER = new CustomAdapter(root, LISTINFO);
                    LIST.setAdapter(ADAPTER);
                    //llenar el LISTINFO desde la api
                    //LISTINFO.add(new ItemList("https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg", "hshnsbdhd", "dggddnd"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
