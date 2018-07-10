package com.example.victor.myproyect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.victor.myproyect.ListDataSource.CustomAdapter;
import com.example.victor.myproyect.ListDataSource.ItemList;
import com.example.victor.myproyect.ListDataSource.OnLoadImage;
import com.example.victor.myproyect.ListDataSource.TaskImg;

import java.util.ArrayList;

public class LoginResult extends AppCompatActivity implements OnLoadImage {
    private String portada, email, nombre,city,phone1,phone2,movil;
    private Context root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        city = this.getIntent().getExtras().getString("city1");
        phone1 = this.getIntent().getExtras().getString("phone11");
        phone2 = this.getIntent().getExtras().getString("phone22");
        movil = this.getIntent().getExtras().getString("movil1");
        portada = this.getIntent().getExtras().getString("portada");
        email = this.getIntent().getExtras().getString("email");
        nombre = this.getIntent().getExtras().getString("nombre");

        loadComponents();
        controlador3();
    }

    private void controlador3(){
        Button btn1 = (Button)this.findViewById(R.id.edit_face);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(), EditPerfilActivity.class);
                reg.putExtra("nombre", nombre);
                reg.putExtra("email",email);
                startActivity(reg);
            }
        });
    }

    private void loadComponents() {
        TextView nametext = (TextView)this.findViewById(R.id.nombre);
        TextView emailtext = (TextView)this.findViewById(R.id.email);
        ImageView img = (ImageView) this.findViewById(R.id.portada);
        Button btn = (Button)this.findViewById(R.id.entrar);
        nametext.setText(nombre);
        emailtext.setText(email);
        TaskImg loadimg = new TaskImg();
        loadimg.execute(portada);
        loadimg.setLoadImage(img,this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(root, MainActivity.class);
                main.putExtra("email",email);
                root.startActivity(main);
            }
        });
    }

    @Override
    public void setLoadImage(ImageView container, Bitmap img) {
        container.setImageBitmap(img);
    }


}