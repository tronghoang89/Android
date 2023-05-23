package com.admin.hoang.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import com.admin.hoang.Adapter.DonHangAdapter;
import com.admin.hoang.R;
import com.admin.hoang.retrofit.ApiBanHang;
import com.admin.hoang.retrofit.RetrofitClient;
import com.admin.hoang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonHangActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView rcdonhang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_hang);
        setView();
        initToolbar();
        getOder();

    }

    private void getOder() {
        compositeDisposable.add(apiBanHang.xemdonhang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter=new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            rcdonhang.setAdapter(adapter);
                        },
                        throwable -> {

                        }
                ));
    }

    private void initToolbar() {
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }



    private void setView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        rcdonhang=findViewById(R.id.recycleview_donhang);
        toolbar=findViewById(R.id.toobar);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rcdonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}