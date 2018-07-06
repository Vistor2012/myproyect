package com.example.victor.myproyect.ListDataSource;

public class ItemList {
    private String image_casa;
    private String detalles_casa;
    private String otro;
    // private String idimdb;
    public ItemList (String image_casa, String detalles_casa, String otro) {
        this.image_casa = image_casa;
        this.detalles_casa = detalles_casa;
        this.otro = otro;
        //this.idimdb = idimdb;
    }
    public String getImage_casa(){
        return this.image_casa;
    }
    public String getDetalles_casa() { return this.detalles_casa; }
    public String getOtro() {
        return this.otro;
    }
    /*public String getIdimdb () {
        return this.idimdb;
    }*/
}
