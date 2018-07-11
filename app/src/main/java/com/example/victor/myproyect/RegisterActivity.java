package com.example.victor.myproyect;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.victor.myproyect.DATA.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.victor.myproyect.DATA.DataApp.HOST_INMUEBLE;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    Button register_data, select_image;
    String path;
    Context root;

    String tipo_ope;

    EditText precio, descripcion, superficie, servicios, direccion;

    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imagen=(ImageView)findViewById(R.id.foto);

        loadcomponents();
        validaPermisos();

        Spinner spinner = (Spinner) findViewById(R.id.tipo_operacion);
        final String[] t_n = {"SELECCIONE", "venta", "alquiler", "anticretico"};
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, t_n));
        String text = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("tipo de operacion");
                tipo_ope = t_n[position];
                System.out.println("ver la operacion");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private void loadcomponents() {
        precio = (EditText)findViewById(R.id.precio);
        descripcion = (EditText)findViewById(R.id.description);
        superficie = (EditText)findViewById(R.id.superficie);
        servicios = (EditText)findViewById(R.id.servicios);
        direccion =  (EditText)findViewById(R.id.direccion);
        select_image = this.findViewById(R.id.select_Image);
        register_data = this.findViewById(R.id.register_data);
        register_data.setOnClickListener(this);
        select_image.setOnClickListener(this);
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
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(contentURI,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri Mipath=data.getData();
                    imagen.setImageURI(Mipath);
                    path = getRealPathFromURI(this,Mipath);
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


    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"entra a on click",Toast.LENGTH_SHORT).show();

        switch (v.getId()){
            //case R.id.select_Image : cargarImagen();break;
            case R.id.select_Image: cargarImagen();break;
            case R.id.register_data:
                Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show();
                try {
                    guardarInfoUser();
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                Toast.makeText(this, "no se selecciono nada", Toast.LENGTH_SHORT).show();
        }

    }

    private void guardarInfoUser() throws FileNotFoundException{
        Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        File file = new File(path);
        RequestParams params = new RequestParams();
        //params.put("img", file);

        params.put("precio", precio.getText());
        params.put("tipo_operacion", tipo_ope);
        params.put("descripcion",descripcion.getText());
        params.put("superficie",superficie.getText());
        params.put("servicios",servicios.getText());
        params.put("direccion", direccion.getText());
        Toast.makeText(getApplicationContext(),"entra aguardar info",Toast.LENGTH_SHORT).show();

        //Aqui hay que cambiar la ip
        String url = "http://192.168.1.3:7777/api/v1.0/" + "inmuebles";

        client.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msn = response.getString("msn");
                    String id = response.getString("id");
                    UserData.IDCasa = id;
                    if (msn != null) {
                        Toast.makeText(root, msn, Toast.LENGTH_SHORT).show();
                        Intent registraPosicion = new Intent(root, latiylongi_Maps.class);
                        registraPosicion.putExtra("id",id);
                        root.startActivity(registraPosicion);
                    } else {
                        Toast.makeText(root, "ERROR AL enviar los datos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(RegisterActivity.this, throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

