package com.atiqur.rgbled.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atiqur.rgbled.databinding.ItemPairedDeviceBinding;
import com.atiqur.rgbled.databinding.ItemPairedDeviceBinding;

import java.util.ArrayList;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.ViewHolder>{

    private ArrayList<ArrayList<String>>deviceObjects;
    private OnItemClick mOnItemClick;
    public PairedDeviceAdapter(ArrayList<ArrayList<String>>objects, OnItemClick onItemClick){
        deviceObjects = objects;
        mOnItemClick = onItemClick;
    }
    @NonNull
    @Override
    public com.atiqur.rgbled.adapter.PairedDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new com.atiqur.rgbled.adapter.PairedDeviceAdapter.ViewHolder(ItemPairedDeviceBinding.inflate((inflater),parent,false),parent.getContext(),mOnItemClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        ItemPairedDeviceBinding binding;
        OnItemClick onItemClick;
        public ViewHolder(ItemPairedDeviceBinding b, Context context, OnItemClick onItemClick) {

            super(b.getRoot());
            binding = b;
            this.context = context;
            this.onItemClick = onItemClick;
            b.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(getAdapterPosition(), deviceObjects);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull com.atiqur.rgbled.adapter.PairedDeviceAdapter.ViewHolder holder, int position) {
        if(deviceObjects.size()==0){
            holder.binding.name.setText("No paired Device");
        }else{
            holder.binding.name.setText(deviceObjects.get(position).get(0));
            holder.binding.address.setText(deviceObjects.get(position).get(1));
        }
    }

    @Override
    public int getItemCount() {
        return deviceObjects.size();
    }

    public interface OnItemClick{
        void onItemClick(int position,ArrayList<ArrayList<String>>objects);
    }
}
