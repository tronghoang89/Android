package com.admin.hoang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.admin.hoang.Adapter.GioHangAdpater;
import com.admin.hoang.R;

import com.admin.hoang.model.EventBus.TinhTongEvent;
import com.admin.hoang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;


public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button bntmuahang;
    GioHangAdpater adpater;
    long totalMoney=0;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();

        if (Utils.muahangs!=null){
            Utils.muahangs.clear();
        }
        TotalMonney();
    }

    private void TotalMonney() {
         totalMoney=0;
        for (int i=0;i<Utils.muahangs.size();i++){
            totalMoney=totalMoney + (Utils.muahangs.get(i).getGiasp())*(Utils.muahangs.get(i).getSoluong());
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(totalMoney));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.gioHangs.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else {
            adpater=new GioHangAdpater(getApplicationContext(),Utils.gioHangs);
            recyclerView.setAdapter(adpater);
        }
        bntmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",totalMoney);
                Utils.gioHangs.clear();
                startActivity(intent);
            }
        });

    }

    private void initView() {
        giohangtrong =findViewById(R.id.txtgiohnagtrong);
        tongtien=findViewById(R.id.txttongtien);
        toolbar=findViewById(R.id.toobargiohang);
        recyclerView=findViewById(R.id.recycleviewgiohang);
        bntmuahang=findViewById(R.id.btnmuahang);
    }
// dung thuw vien Even Bus de tinh tong tien

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
     @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if (event !=null){
            TotalMonney();
        }
     }

}