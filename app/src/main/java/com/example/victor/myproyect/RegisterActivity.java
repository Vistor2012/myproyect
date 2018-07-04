package com.example.victor.myproyect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    //Button botonCargar;
    //Button register_data;
    String path;

    EditText precio, descripcion, superficie, servicios;

    Spinner categor;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        precio = (EditText)findViewById(R.id.precio);
        descripcion = (EditText)findViewById(R.id.description);
        superficie = (EditText)findViewById(R.id.superficie);
        servicios = (EditText)findViewById(R.id.servicios);

        /*register_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String precio1 = precio.getText().toString();
                String descripcion1 = descripcion.getText().toString();
                String superficie1 = superficie.getText().toString();
                String servicios1 = servicios.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, precio1);
                intent.putExtra(Intent.EXTRA_TEXT, descripcion1);
                intent.putExtra(Intent.EXTRA_TEXT, superficie1);
                intent.putExtra(Intent.EXTRA_TEXT, servicios1);

                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Select Register app"));

            }
        });*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categor = (Spinner)findViewById(R.id.cat);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.Cat,android.R.layout.simple_spinner_item);
        categor.setAdapter(adapter);
        categor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imagen=(ImageView)findViewById(R.id.foto);

        irMapa();
    }

    private void irMapa() {
        Button btn5 = (Button)this.findViewById(R.id.ubicacion);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubi = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(ubi);
            }
        });
    }


    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegisterActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+ File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

        startActivityForResult(intent,COD_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri Mipath=data.getData();
                    imagen.setImageURI(Mipath);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ruta de almacenamiento","Path: "+path);

                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);

                    break;
            }
        }
    }
}
