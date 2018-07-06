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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.myproyect.DATA.DataApp;
import com.example.victor.myproyect.DATA.UserData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditPerfilActivity extends AppCompatActivity implements  View.OnClickListener {
    private final String CARPETA_RAIZ1="misImagenesPrueba1/";
    private final String RUTA_IMAGENES=CARPETA_RAIZ1+"miPerfil";

    final int COD_SELECCIONA1=12;
    final int COD_FOTOGRAFIA=15;

    String path1;

    private String email_user, last_name;
    private Context root;

    ImageView imagen1;
    Button sel_img , save;
    EditText city,phone1,phone2,movil;
    TextView emailtext,nametext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        root = this;
        super.onCreate(savedInstanceState);
        validaPermisos();
        setContentView(R.layout.activity_edit_perfil);

        imagen1=(ImageView)findViewById(R.id.foto1);

        email_user = this.getIntent().getExtras().getString("email");
        last_name = this.getIntent().getExtras().getString("nombre");
        //Toast.makeText(root, last_name, Toast.LENGTH_SHORT).show();

        loadComponents();

    }

    private void loadComponents() {
        nametext = (TextView)this.findViewById(R.id.name_lastname);
        emailtext = (TextView)this.findViewById(R.id.email_u);
        city = this.findViewById(R.id.city);
        phone1 = this.findViewById(R.id.phone1);
        phone2 = this.findViewById(R.id.phone2);
        movil = this.findViewById(R.id.movil);
        sel_img = this.findViewById(R.id.select_im_perf);
        save = this.findViewById(R.id.save);
        save.setOnClickListener(this);
        sel_img.setOnClickListener(this);

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

    private void cargarImagen1() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(EditPerfilActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                if (opciones[j].equals("Tomar Foto")){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_im_perf : cargarImagen1();break;
            case R.id.save :
                try {
                    guardarInfoUser();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(this, "no se selecciono nada", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarInfoUser() throws FileNotFoundException {
        Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        File file = new File(path1);
        RequestParams params = new RequestParams();
        params.put("img", file);

        params.put("name",nametext.getText());
        params.put("email",emailtext.getText());
        params.put("ciudad", city.getText());
        params.put("phone",phone1.getText());
        params.put("phone2",phone2.getText());
        params.put("movil", movil.getText());

        params.put("ciudad",city.getText());

        //Aqui hay que cambiar la ip
        client.post(DataApp.HOST+"agenteVentas", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msn = response.getString("msn");
                    String id = response.getString("id");

                    UserData.ID = id;
                    if (msn != null) {
                        Toast.makeText(root, msn, Toast.LENGTH_SHORT).show();
                        Intent main = new Intent(root, MainActivity.class);
                        root.startActivity(main);
                    } else {
                        Toast.makeText(root, "ERROR AL enviar los datos", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
