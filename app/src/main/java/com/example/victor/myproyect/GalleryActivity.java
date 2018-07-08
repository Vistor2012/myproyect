package com.example.victor.myproyect;


import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.victor.myproyect.ListDataSource.CustomAdapter;
import com.example.victor.myproyect.ListDataSource.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class GalleryActivity extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private Context root;
    private CustomAdapter ADAPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        root = this;

        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        loadInitialRestData();
        loadcomponents();

    }

    private void loadInitialRestData() {
        AsyncHttpClient client = new AsyncHttpClient();
        //aqui donde tiene q cargar la informacion
        String url = "http://192.168.1.2:7777/api/v1.0/" + "inmuebles";
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray list = (JSONArray) response.get("search_house");
                    for(int i = 0; i < list.length(); i++){
                        JSONObject itemJson = list.getJSONObject(i);
                        String descripcion = itemJson.getString("detalles_casa");
                        String servicios = itemJson.getString("servicios_p");
                        String precio = itemJson.getString("precio_p");
                        String superficie = itemJson.getString("superficie_p");
                        String direccion = itemJson.getString("direccion_p");
                        String imdbID = itemJson.getString("imdbID");
                        String images = itemJson.getString("image_casa");

                        ItemList item = new ItemList(images, descripcion, servicios, precio, superficie, direccion, imdbID);
                        LISTINFO.add(item);
                    }
                    //llenar el LISTINFO desde la api
                    //LISTINFO.add(new ItemList("https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg", "hshnsbdhd", "dggddnd"));

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }
        });
    }

    private void loadcomponents() {
        LIST = (ListView) this.findViewById(R.id.list_galery);
        ADAPTER = new CustomAdapter(root, LISTINFO);
        LIST.setAdapter(ADAPTER);
    }

}