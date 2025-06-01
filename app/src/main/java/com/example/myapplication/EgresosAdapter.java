package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EgresosAdapter extends RecyclerView.Adapter<EgresosAdapter.EgresoViewHolder> {
    private final List<EgresoItem> egresoList;

    public EgresosAdapter(List<EgresoItem> egresoList){
        this.egresoList = egresoList;
    }

    @NonNull
    @Override
    public EgresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        return new EgresoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EgresoViewHolder holder, int position){
        EgresoItem egreso = egresoList.get(position);
        holder.txtCategoria.setText(egreso.getCategoria());
        holder.txtCantidad.setText(String.valueOf(egreso.getCantidad()));
    }

    @Override
    public  int getItemCount(){
        return egresoList.size();
    }

    public static class EgresoViewHolder extends RecyclerView.ViewHolder{
        TextView txtCantidad, txtCategoria;

        public EgresoViewHolder(@NonNull View itemView){
            super(itemView);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
        }
    }
}
