package com.example.myapplication;

import com.google.firebase.Timestamp;

public class MetaFinancieraModel {
        private String id;
        private String cantidad_objetivo;
        private String motivo_ahorro;
        private String plazo;
        private Timestamp timestamp;

        public MetaFinancieraModel() {}

        // Getters y setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCantidad_objetivo() { return cantidad_objetivo; }
        public void setCantidad_objetivo(String cantidad_objetivo) { this.cantidad_objetivo = cantidad_objetivo; }

        public String getMotivo_ahorro() { return motivo_ahorro; }
        public void setMotivo_ahorro(String motivo_ahorro) { this.motivo_ahorro = motivo_ahorro; }

        public String getPlazo() { return plazo; }
        public void setPlazo(String plazo) { this.plazo = plazo; }

        public Timestamp getTimestamp() { return timestamp; }
        public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    }


