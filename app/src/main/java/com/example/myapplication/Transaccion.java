package com.example.myapplication;

public class Transaccion {

    private String id;
    private String fecha;
    private String subcategoria;
    private String categoria;
    private String concepto;
    private double cantidad;
    private String comentario;
    private String plan_pagos;

    // Constructor vacío requerido por Firestore
    public Transaccion() {}

    // Getters y setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getPlan_pagos(){ return plan_pagos; }
    public void setPlan_pagos(String plan_pagos){ this.plan_pagos = plan_pagos; }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
}

