package com.example.victor.myproyect;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.victor.myproyect.ListDataSource.CustomAdapter;
import com.example.victor.myproyect.ListDataSource.ItemList;

import java.util.ArrayList;


public class GalleryActivity extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemList> LISTINFO;
    private CustomAdapter ADAPTER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LISTINFO = new ArrayList<ItemList>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        loadcomponents();

    }

    private void loadcomponents() {
        LIST = (ListView) this.findViewById(R.id.list_galery);

        //llenar el LISTINFO desde la api
        LISTINFO.add(new ItemList("https://www.construyehogar.com/wp-content/uploads/2017/10/Fachada-de-casa-moderna-peque%C3%B1a.jpg", "hshnsbdhd", "dggddnd"));

        ADAPTER = new CustomAdapter(this, LISTINFO);
        LIST.setAdapter(ADAPTER);
    }

}
