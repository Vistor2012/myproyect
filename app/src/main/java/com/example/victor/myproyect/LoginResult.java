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
import android.widget.TextView;

import com.example.victor.myproyect.ListDataSource.OnLoadImage;
import com.example.victor.myproyect.ListDataSource.TaskImg;

public class LoginResult extends AppCompatActivity implements OnLoadImage {
    private String portada, email, nombre;
    private Context root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        portada = this.getIntent().getExtras().getString("portada");
        email = this.getIntent().getExtras().getString("email");
        nombre = this.getIntent().getExtras().getString("nombre");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadComponents();
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
                root.startActivity(main);
            }
        });
    }

    @Override
    public void setLoadImage(ImageView container, Bitmap img) {
        container.setImageBitmap(img);
    }
}
