package com.admin.hoang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.admin.hoang.R;
import com.admin.hoang.model.Giohang;
import com.admin.hoang.model.SanPhamMoi;
import com.admin.hoang.utils.Utils;
import com.bumptech.glide.Glide;
//import com.example.hoang.R;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp;
    TextView giasp;
    TextView mota;
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

                AddItems();
            }

        });
    }

    private void AddItems(){
        if (Utils.gioHangs.size()>0){
            boolean flag=false;
            int soluong =Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i=0;i<Utils.gioHangs.size();i++)
            {
                if (Utils.gioHangs.get(i).getIdsp()==sanPhamMoi.getId()){
                    Utils.gioHangs.get(i).setSoluong(soluong+Utils.gioHangs.get(i).getSoluong());
                    String str_gia=sanPhamMoi.getGiasp().replace(".","");
                    long gia= Long.parseLong(str_gia)* Utils.gioHangs.get(i).getSoluong();
                    Utils.gioHangs.get(i).setGiasp(gia);
                    flag=true;
                }
            }
            if (flag==false){
                String str_gia=sanPhamMoi.getGiasp().replace(".","");
                long gia=Long.parseLong(str_gia)*soluong;
                Giohang gioHang =new Giohang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.gioHangs.add(gioHang);
            }
        }
        else {
            int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
            String str_gia = sanPhamMoi.getGiasp().replace(".","");
            long gia=Long.parseLong(str_gia)*soluong; // hic sang tao khong phai kieu nen gio loi te le
            Giohang gioHang=new Giohang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.gioHangs.add(gioHang);
        }
        // so luong mua hang
            int total=0;
            for (int i=0;i<Utils.gioHangs.size();i++){
                total=total+Utils.gioHangs.get(i).getSoluong();
            }
        badge.setText(String.valueOf(total));
    }


    private void initData() {
         sanPhamMoi= (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        DecimalFormat df = new DecimalFormat("###,###,###");
        //giasp.setText("Gia" +df.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"d" );
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

        FrameLayout frameLayoutgiohang=findViewById(R.id.frameCart);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.gioHangs!=null){
            int total=0;
            for (int i=0;i<Utils.gioHangs.size();i++){
                total=total+Utils.gioHangs.get(i).getSoluong();
            }
            badge.setText(String.valueOf(Utils.gioHangs));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.gioHangs!=null){
            int total=0;
            for (int i=0;i<Utils.gioHangs.size();i++){
                total=total+Utils.gioHangs.get(i).getSoluong();
            }
            badge.setText(String.valueOf(Utils.gioHangs));
        }
    }
}