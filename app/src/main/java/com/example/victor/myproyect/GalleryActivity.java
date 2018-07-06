package com.example.victor.myproyect;


import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.victor.myproyect.DATA.DataApp;
import com.example.victor.myproyect.ItemMenu.ItemMenuStructure;
import com.example.victor.myproyect.ItemMenu.MenuBaseAdapter;
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
    private MenuBaseAdapter ADAPTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //loadcomponents();
        getHomeData();
    }

    private void loadcomponents() {
        LIST = (ListView) this.findViewById(R.id.list_galery);

        //llenar el LISTINFO desde la api
        //LISTINFO.add(new ItemList("https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg", "hshnsbdhd", "dggddnd"));

        ADAPTER = new MenuBaseAdapter(this, DataApp.LISDATA);
        LIST.setAdapter(ADAPTER);


    }

    public void getHomeData() {
        AsyncHttpClient client = new AsyncHttpClient();
        DataApp.LISDATA = new ArrayList<ItemMenuStructure>();
        client.get(DataApp.HOST_INMUEBLE, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray listData = response.getJSONArray("docs");
                    for (int i = 0; i < listData.length(); i++) {
                        JSONObject obj = listData.getJSONObject(i);
                        String precio = obj.getString("precioIso");
                        String descripcion = obj.getString("descripcion");

                        String supterreno = obj.getString("supterreno");
                        JSONArray images = obj.getJSONArray("images");

                        String id = obj.getString("_id");
                        ArrayList<String> urllist =  new ArrayList<String>();
                        for (int j = 0; j < images.length(); j ++) {
                            urllist.add(DataApp.HOST_ROOT +images.getString(j));
                        }
                        Toast.makeText(GalleryActivity.this, id, Toast.LENGTH_SHORT).show();



                        DataApp.LISDATA.add(new ItemMenuStructure(supterreno,urllist,2521,0.5,0.5,"", "","",id,descripcion));

                    }
                    loadcomponents();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }
}

