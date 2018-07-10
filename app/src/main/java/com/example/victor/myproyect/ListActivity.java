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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        String url = "http://192.168.1.7:7777/api/v1.0/inmuebles";
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
                        String descripcion = itemJson.getString("descripcion");
                        String servicios = itemJson.getString("servicios");
                        String precio = itemJson.getString("precio");
                        String superficie = itemJson.getString("superficie");
                        String tipo_operacion = itemJson.getString("tipo_operacion");
                        String direccion = itemJson.getString("direccion");
                        //String imdbID = itemJson.getString("_id");
                        //String images = itemJson.getString("image_casa");

                        ItemList item = new ItemList(descripcion, servicios, precio, superficie, direccion, tipo_operacion);
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
