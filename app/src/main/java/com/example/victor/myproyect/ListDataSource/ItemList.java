package com.example.victor.myproyect.ListDataSource;

public class ItemList {
    private String image_casa;
    private String detalles_casa;
    private String servicios_p;
    private String precio_p;
    private String superficie_p;
    private String direccion_p;
    private String idimdb;
    public ItemList (String image_casa, String detalles_casa, String servicios_p, String precio_p, String superficie_p, String direccion_p, String idimdb) {
        this.image_casa = image_casa;
        this.detalles_casa = detalles_casa;
        this.servicios_p = servicios_p;
        this.precio_p = precio_p;
        this.superficie_p = superficie_p;
        this.direccion_p = direccion_p;
        this.idimdb = idimdb;
    }
    public String getImage_casa(){
        return this.image_casa;
    }
    public String getDetalles_casa() { return this.detalles_casa; }
    public String getServicios_p() {
        return this.servicios_p;
    }
    public String getPrecio_p() {
        return this.precio_p;
    }
    public String getSuperficie_p() {
        return this.superficie_p;
    }
    public String getDireccion_p() { return this.direccion_p; }
    public String getIdimdb () {
        return this.idimdb;
    }
}
