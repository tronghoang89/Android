package com.example.hoang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hoang.R;
import com.example.hoang.model.GioHang;
import com.example.hoang.model.SanPhamMoi;
import com.example.hoang.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btbthem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
        
    }

    private void initControl() {
        btbthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.gioHang.size()>0){
            boolean flag =false;
            int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i=0;i<Utils.gioHang.size();i++){
                if (Utils.gioHang.get(i).getIdsp()==sanPhamMoi.getId());
                {
                     Utils.gioHang.get(i).setSoluong(soluong+Utils.gioHang.get(i).getSoluong());
                     long gia =Long.parseLong(sanPhamMoi.getGiasp())*Utils.gioHang.get(i).getSoluong();
                    Utils.gioHang.get(i).setGiasp(gia);
                    flag=true;

                }
            }
            if  (flag==false){
                long gia= Long.parseLong(sanPhamMoi.getGiasp())*soluong;
                GioHang gioHang=new GioHang();
                gioHang.getGiasp(gia);
                gioHang.getSoluong();
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.gioHang.add(gioHang);
            }
        }else {
            int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
            long gia=Long.parseLong(sanPhamMoi.getGiasp())*soluong;
//            long gia= Long.parseLong(sanPhamMoi.getGiasp())*soluong;
            GioHang gioHang=new GioHang();
            gioHang.getGiasp(gia);
            gioHang.getSoluong();
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.gioHang.add(gioHang);
        }
        badge.setText(String.valueOf(Utils.gioHang.size()));
    }

    private void initData() {
        SanPhamMoi sanPhamMoi= (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        giasp.setText(sanPhamMoi.getGiasp());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        Integer[] so=new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp=findViewById(R.id.txttenSpchitiet);
        giasp=findViewById(R.id.txtgiaspchitiet);
        mota=findViewById(R.id.txtmotachitiet);
        btbthem=findViewById(R.id.buttomthemvaogiohang);
        spinner=findViewById(R.id.spiner);
        imghinhanh=findViewById(R.id.imgchitiet);
        toolbar=findViewById(R.id.toobar);
        badge=findViewById(R.id.menu_sl);

    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ket thuc va tra ve
                finish();
            }
        });
    }
}