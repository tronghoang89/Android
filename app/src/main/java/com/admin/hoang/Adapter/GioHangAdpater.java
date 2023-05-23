package com.admin.hoang.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.hoang.Interface.ImageClickListenner;
import com.admin.hoang.R;
import com.admin.hoang.model.EventBus.TinhTongEvent;
import com.admin.hoang.model.Giohang;
import com.admin.hoang.utils.Utils;
import com.bumptech.glide.Glide;


import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdpater extends RecyclerView.Adapter<GioHangAdpater.MyViewHolder> {
    Context context;
    List<Giohang>gioHangList;

    public GioHangAdpater(Context context, List<Giohang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Giohang giohang=gioHangList.get(position);
        holder.item_giohang_tensp.setText(giohang.getTensp());
        holder.item_giohnag_soluong.setText(giohang.getSoluong()+" ");
        Glide.with(context).load(giohang.getHinhsp()).into(holder.item_giohnag_image);
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        holder.item_giohnag_gia.setText(decimalFormat.format((giohang.getGiasp())));
        long gia=giohang.getSoluong()*giohang.getGiasp();
        holder.item_giohnag_giasp2.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Utils.muahangs.add(giohang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
                else {
                    for (int i=0;i<Utils.muahangs.size();i++){
                        if (Utils.muahangs.get(i).getIdsp()==giohang.getIdsp()){
                            Utils.muahangs.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
        holder.setImageClickListenner(new ImageClickListenner() {
            // Ham cong tru cua cai gio hang
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("Tag","onImageClick: "+pos +".... "+giatri);
                if (giatri==1){
                    if (gioHangList.get(pos).getSoluong()>1){
                        int soluongmoi=gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohnag_soluong.setText(gioHangList.get(pos).getSoluong()+" ");
                        long gia=gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                        holder.item_giohnag_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }
                    else if (gioHangList.get(pos).getSoluong()==1){
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thong bao ");
                        builder.setMessage("Ban co muon xoa san pham khong");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.gioHangs.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }

                }
                else if (giatri==2){
                    if (gioHangList.get(pos).getSoluong()<11){
                        int soluongmoi= gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                }
                holder.item_giohnag_soluong.setText(gioHangList.get(pos).getSoluong()+" ");
                long gia=gioHangList.get(pos).getSoluong()*gioHangList.get(pos).getGiasp();
                holder.item_giohnag_giasp2.setText(decimalFormat.format(gia));
                EventBus.getDefault().postSticky(new TinhTongEvent());

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohnag_image,imgtru,imgcong;
        TextView item_giohang_tensp,item_giohnag_gia,item_giohnag_soluong,item_giohnag_giasp2;
        ImageClickListenner imageClickListenner;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgtru=itemView.findViewById(R.id.item_gihang_tru);
            imgcong=itemView.findViewById(R.id.item_gihang_cong);
            item_giohnag_image=itemView.findViewById(R.id.item_giohnag_image);
            item_giohang_tensp=itemView.findViewById(R.id.item_giohang_tensp);
            item_giohnag_gia=itemView.findViewById(R.id.item_giohnag_gia);
            item_giohnag_soluong=itemView.findViewById(R.id.item_giohnag_soluong);
            item_giohnag_giasp2=itemView.findViewById(R.id.item_giohnag_giasp2);
            checkBox=itemView.findViewById(R.id.item_giohang_check);
            //even clich them bot so luong
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setImageClickListenner(ImageClickListenner imageClickListenner) {
            this.imageClickListenner = imageClickListenner;
        }

        @Override
        public void onClick(View view) {
            if (view==imgtru){
                imageClickListenner.onImageClick(view,getAdapterPosition(),1);
                //1 tru
            }
            else if(view==imgcong){
                imageClickListenner.onImageClick(view,getAdapterPosition(),2);
                //cong la gia tri 2
            }

        }
    }

}

