package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.ui.metafin_itemDetails;
import java.util.ArrayList;
import java.util.List;

    public class MetaFinancieraAdapter extends RecyclerView.Adapter<com.example.myapplication.MetaFinancieraAdapter.MetaFinancieraViewHolder> {
        private List<MetaFinancieraModel> metasFinancierasList;
        private final List<MetaFinancieraModel> fullList;
        private final Context context;
        private final String tipo;

        public MetaFinancieraAdapter(List<MetaFinancieraModel> metasFinancierasList, Context context, String tipo){
            this.metasFinancierasList = metasFinancierasList;
            this.context = context;
            this.tipo = tipo;
            this.fullList = new ArrayList<>(metasFinancierasList);
        }

        @NonNull
        @Override
        public MetaFinancieraAdapter.MetaFinancieraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meta_financiera, parent, false);
            return new MetaFinancieraAdapter.MetaFinancieraViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MetaFinancieraAdapter.MetaFinancieraViewHolder holder, int position){
            MetaFinancieraModel MFmodel = metasFinancierasList.get(position);
            holder.txtCantidadObj.setText(MFmodel.getCantidad_objetivo());
            holder.txtMotivo.setText(String.valueOf(MFmodel.getMotivo_ahorro()));
            holder.txtPlazo.setText(MFmodel.getPlazo());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, metafin_itemDetails.class);
                intent.putExtra("id", MFmodel.getId());
                intent.putExtra("coleccion", tipo);
                intent.putExtra("cantidad_objetivo", MFmodel.getCantidad_objetivo());
                intent.putExtra("motivo_ahorro", MFmodel.getMotivo_ahorro());
                intent.putExtra("plazo", MFmodel.getPlazo());
                ((Activity) context).startActivityForResult(intent, -1);
            });
        }

        @Override
        public int getItemCount(){
            return metasFinancierasList.size();
        }

        public static class MetaFinancieraViewHolder extends RecyclerView.ViewHolder {
            TextView txtCantidadObj, txtMotivo, txtPlazo;

            public MetaFinancieraViewHolder(@NonNull View itemView){
                super(itemView);
                txtCantidadObj = itemView.findViewById(R.id.txtCantidadObjetivo);
                txtMotivo = itemView.findViewById(R.id.txtMotivo);
                txtPlazo = itemView.findViewById(R.id.txtPlazo);
            }
        }
    }


