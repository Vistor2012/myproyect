package com.example.victor.myproyect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditPerfilActivity extends AppCompatActivity {
    private final String CARPETA_RAIZ1="misImagenesPrueba1/";
    private final String RUTA_IMAGENES=CARPETA_RAIZ1+"miPerfil";

    final int COD_SELECCIONA1=12;
    final int COD_FOTOGRAFIA=15;

    String path1;

    private String email_user, last_name;
    private Context root;

    ImageView imagen1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        validaPermisos();
        setContentView(R.layout.activity_edit_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imagen1=(ImageView)findViewById(R.id.foto1);

        email_user = this.getIntent().getExtras().getString("email");
        last_name = this.getIntent().getExtras().getString("nombre");
        Toast.makeText(root, last_name, Toast.LENGTH_SHORT).show();
        loadComponents();

    }

    private void loadComponents() {
        TextView nametext = (TextView)this.findViewById(R.id.name_lastname);
        TextView emailtext = (TextView)this.findViewById(R.id.email_u);

        nametext.setText(last_name);
        emailtext.setText(email_user);
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);


        return false;
    }
    public void onclick(View view) {
        cargarImagen1();
    }

    private void cargarImagen1() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(EditPerfilActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                if (opciones[j].equals("Tomar Fotografia")){
                    tomarImagenes();
                }else{
                    if (opciones[j].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA1);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarImagenes() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGENES);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path1=Environment.getExternalStorageDirectory()+ File.separator+RUTA_IMAGENES+File.separator+nombreImagen;

        File imagen=new File(path1);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

        startActivityForResult(intent,COD_FOTOGRAFIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA1:
                    Uri Mipath=data.getData();
                    imagen1.setImageURI(Mipath);
                    break;
                case COD_FOTOGRAFIA:
                    MediaScannerConnection.scanFile(this, new String[]{path1}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ruta de almacenamiento","Path: "+path);

                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path1);
                    imagen1.setImageBitmap(bitmap);

                    break;
            }
        }
    }

}
