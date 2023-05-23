package com.admin.hoang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.hoang.R;
import com.admin.hoang.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
    Context context;
   List<DonHang>donHangs;

    public DonHangAdapter(Context context, List<DonHang> donHangs) {
        this.context = context;
        this.donHangs = donHangs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang=donHangs.get(position);
        holder.txtdonhang.setText("Don hang :"+donHang.getId());
        LinearLayoutManager layoutManager=new LinearLayoutManager(
                holder.rcchitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        // adpater chi tiet

        ChiTietAdapter chiTietAdapter=new ChiTietAdapter(context,donHang.getItem());
        holder.rcchitiet.setLayoutManager(layoutManager);
        holder.rcchitiet.setAdapter(chiTietAdapter);
        holder.rcchitiet.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return donHangs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang;
        RecyclerView rcchitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang=itemView.findViewById(R.id.iddonhang);
            rcchitiet=itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}
