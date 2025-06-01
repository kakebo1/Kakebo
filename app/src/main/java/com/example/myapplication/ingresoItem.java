package com.example.myapplication;

public class ingresoItem {
    private String categoria;
    private double cantidad;

    public ingresoItem() {
    }

    public ingresoItem(String categoria, double cantidad, String concepto, String fecha, String comentario, String subcategoria) {
        this.categoria = categoria;
        this.cantidad = cantidad;
    }

    public String getCategoria(){
        return categoria;
    }

    public double getCantidad(){
        return cantidad;
    }


    /*Los setters pueden ser opcionales, depende si se requiere actualizar los datos
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCantidad(double cantidad){
        this.cantidad = cantidad;
    } */
}
