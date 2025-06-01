package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {
     private final List<Transaccion> transaccionList;

        public TransaccionAdapter(List<Transaccion> transaccionList){
            this.transaccionList = transaccionList;
        }

        @NonNull
        @Override
        public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
            return new TransaccionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position){
            Transaccion transaccion = transaccionList.get(position);
            holder.txtCategoria.setText(transaccion.getCategoria());
            holder.txtCantidad.setText(String.valueOf(transaccion.getCantidad()));
        }

        @Override
        public  int getItemCount(){
            return transaccionList.size();
        }

        public static class TransaccionViewHolder extends RecyclerView.ViewHolder{
            TextView txtCantidad, txtCategoria;

            public TransaccionViewHolder(@NonNull View itemView){
                super(itemView);
                txtCantidad = itemView.findViewById(R.id.txtCantidad);
                txtCategoria = itemView.findViewById(R.id.txtCategoria);
            }
        }
    }
