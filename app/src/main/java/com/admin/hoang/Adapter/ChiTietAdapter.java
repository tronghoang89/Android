package com.admin.hoang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.hoang.R;
import com.bumptech.glide.Glide;

import com.admin.hoang.model.Item;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    Context context;
    List<Item>itemList;

    public ChiTietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.txtten.setText(item.getTensp()+ " ");
        holder.txtsoluong.setText("So luong: "+item.getSoluong()+" ");
        Glide.with(context).load(item.getHinhanh()).into(holder.item_imgdonhang);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView item_imgdonhang;
        TextView txtten,txtsoluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_imgdonhang=itemView.findViewById(R.id.item_imgdonhang);
            txtten =itemView.findViewById(R.id.tensp_donhang);
            txtsoluong=itemView.findViewById(R.id.soluong_donhang);
        }
    }
}
