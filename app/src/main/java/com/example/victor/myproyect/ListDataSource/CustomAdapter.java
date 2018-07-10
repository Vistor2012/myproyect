package com.example.victor.myproyect.ListDataSource;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victor.myproyect.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{

    private Context CONTEXT;
    private ArrayList<ItemList>LIST;
    public CustomAdapter(Context contex, ArrayList<ItemList>list){
        this.CONTEXT = contex;
        this.LIST = list;

    }

    @Override
    public int getCount() {

        return this.LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return this.LIST.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflate = (LayoutInflater) this.CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.content_detaills , null);

        }
        TextView detalles = (TextView)convertView.findViewById(R.id.details_house);
        TextView servicios = (TextView)convertView.findViewById(R.id.servicios_p);
        TextView precio = (TextView)convertView.findViewById(R.id.precio_p);
        TextView superficie = (TextView)convertView.findViewById(R.id.superficie_p);
        TextView tipo_operacion = (TextView)convertView.findViewById(R.id.tipo_operacion);
        TextView direccion = (TextView)convertView.findViewById(R.id.direccion_p);

        detalles.setText(this.LIST.get(position).getDetalles_casa());
        servicios.setText(this.LIST.get(position).getServicios_p());
        precio.setText(this.LIST.get(position).getPrecio_p());
        superficie.setText(this.LIST.get(position).getSuperficie_p());
        tipo_operacion.setText(this.LIST.get(position).getTipo_operacion());
        direccion.setText(this.LIST.get(position).getDireccion_p());

        ImageView img = (ImageView)convertView.findViewById(R.id.image_house);

        //sin hilo la imagen
        try {
            URL url = new URL(this.LIST.get(position).getImage_casa());
            InputStream stream = url.openConnection().getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(stream);
            img.setImageBitmap(imageBitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //revisar con Hilo la imagen

        /*ImageView img = (ImageView)convertView.findViewById(R.id.image_house);
        TaskImg hilo = new TaskImg();
        hilo.setLoadImage(img, this);
        hilo.execute(this.LIST.get(position).getImage_casa());*/
        //
        return convertView;
    }
}
