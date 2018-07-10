package com.example.victor.myproyect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DetaillsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaills);

        Button btn1 = (Button)this.findViewById(R.id.dialog_email);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(DetaillsActivity.this, MailActivity.class);
                startActivity(reg);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.phone_call);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:65490772"));
                if(ActivityCompat.checkSelfPermission(DetaillsActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                    return;
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"entra a email",Toast.LENGTH_SHORT).show();

    }
}
