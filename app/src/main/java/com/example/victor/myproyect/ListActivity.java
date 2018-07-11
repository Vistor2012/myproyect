package com.example.victor.myproyect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.victor.myproyect.DATA.DataApp;
import com.example.victor.myproyect.ListDataSource.CustomAdapter;
import com.example.victor.myproyect.ListDataSource.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        /*/*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.dialog_email);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, MailActivity.class);
                startActivity(i);
            }
        });
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.phone_call);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("65490772"));
                if(ActivityCompat.checkSelfPermission(ListActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                    return;
                startActivity(i);
            }
        });*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        root = this;
        LISTINFO = new ArrayList<ItemList>();
        LIST =(ListView) this.findViewById(R.id.list_house);
        LIST.setOnItemClickListener( this);


        //loadInitialRestData();
        loadcomponents();
    }

    private void loadcomponents() {
        EditText search = (EditText)this.findViewById(R.id.search_house);
        //eventos
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                loadInitialRestData("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                loadInitialRestData(str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //LISTINFO.add(new ItemList("https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg", "hshnsbdhd", "dggddnd", "jddnd", "gdhd", "venta", "bfjj"));
        //ADAPTER = new CustomAdapter(this,LISTINFO);
        //LIST.setAdapter(ADAPTER);
    }

    private void loadInitialRestData(String keystr) {
        AsyncHttpClient client = new AsyncHttpClient();
        // Toast.makeText(getApplicationContext(),"esta entrando aqui",Toast.LENGTH_SHORT).show();
        //aqui donde tiene q cargar la infarmacion
        String url = DataApp.HOST +keystr+ "inmuebles";
        //Toast.makeText(root, url, Toast.LENGTH_LONG).show();
        //String descripcion="";
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                //Toast.makeText(getApplicationContext(),"entra",Toast.LENGTH_SHORT).show();

                try {
                    for(int i = 0; i < response.length(); i++) {
                        //Toast.makeText(getApplicationContext(),"esta entrando aqui",Toast.LENGTH_SHORT).show();
                        JSONObject itemJson = response.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),itemJson+"",Toast.LENGTH_SHORT).show();
                        String descripcion_p = itemJson.getString("descripcion");
                        String servicios_p = itemJson.getString("servicios");
                        String precio_p = itemJson.getString("precio");
                        String superficie_p = itemJson.getString("superficie");
                        String tipo_operacion = itemJson.getString("tipo_operacion");
                        String direccion_p = itemJson.getString("direccion");
                        //String imdbID = itemJson.getString("_id");
                        String images = itemJson.getString("images");
                        //"https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg"
                        ItemList item = new ItemList(DataApp.HOST_ROOT+images,descripcion_p, servicios_p, precio_p, superficie_p, tipo_operacion, direccion_p);
                        LISTINFO.add(item);
                    }

                    loadList();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadList() {
        ADAPTER = new CustomAdapter(root, LISTINFO);
        LIST.setAdapter(ADAPTER);

        LIST.setOnItemClickListener( this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(root, "msj", Toast.LENGTH_SHORT).show();
    }
}
