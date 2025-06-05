package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ui.itemDetails;

import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {
    private final List<Transaccion> transaccionList;
    private final Context context;
    private final String tipo; // ingresos o plan_ingresos

    public TransaccionAdapter(List<Transaccion> transaccionList, Context context, String tipo){
        this.transaccionList = transaccionList;
        this.context = context;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position){
        Transaccion transaccion = transaccionList.get(position);
        holder.txtCategoria.setText(transaccion.getCategoria());
        holder.txtCantidad.setText(String.valueOf(transaccion.getCantidad()));

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, itemDetails.class);

            intent.putExtra("id", transaccion.getId());
            intent.putExtra("coleccion", tipo);
            intent.putExtra("fecha", transaccion.getFecha());
            intent.putExtra("categoria", transaccion.getCategoria());
            intent.putExtra("subcategoria", transaccion.getSubcategoria());
            intent.putExtra("concepto", transaccion.getConcepto());
            intent.putExtra("cantidad", transaccion.getCantidad());
            intent.putExtra("comentario", transaccion.getComentario());

            if (tipo.equals("pago_deuda") || tipo.equals("plan_deuda")) {
                intent.putExtra("plan_pagos", transaccion.getPlan_pagos());
            }

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount(){
        return transaccionList.size();
    }

    public static class TransaccionViewHolder extends RecyclerView.ViewHolder {
        TextView txtCantidad, txtCategoria;

        public TransaccionViewHolder(@NonNull View itemView){
            super(itemView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
        }
    }
}

