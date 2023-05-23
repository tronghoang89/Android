package com.admin.hoang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.hoang.R;
import com.admin.hoang.retrofit.RetrofitClient;
import com.admin.hoang.utils.Utils;
import com.admin.hoang.retrofit.ApiBanHang;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttien,txtematl,txtsdt;
    EditText eddiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        intControl();

    }

    private void countItem() {
         total=0;
        for (int i = 0; i< Utils.muahangs.size(); i++){
            total=total+Utils.muahangs.get(i).getSoluong();
        }
    }

    private void intControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        long tongtien=getIntent().getLongExtra("tongtien",0);
        txttien.setText(decimalFormat.format(tongtien));
        txtematl.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getMobile());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi=eddiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Ban chua nhap dia chi", Toast.LENGTH_SHORT).show();
                }
                else {
                    //pos data
                    String str_email=Utils.user_current.getEmail();
                    String str_mobile=Utils.user_current.getMobile();
                    int id=Utils.user_current.getId();
                    Log.d("test",new Gson().toJson(Utils.muahangs));
                    compositeDisposable.add(apiBanHang.CreateOrder(str_email,str_mobile,String.valueOf(tongtien),id,str_diachi,total,new Gson().toJson(Utils.muahangs))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar=findViewById(R.id.toobarthanhtoan);
        txttien=findViewById(R.id.txtgiahang);
        txtsdt=findViewById(R.id.txtsdtdathang);
        txtematl=findViewById(R.id.txtemaildathang);
        eddiachi=findViewById(R.id.diachigiaohang);
        btndathang=findViewById(R.id.btndathang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}