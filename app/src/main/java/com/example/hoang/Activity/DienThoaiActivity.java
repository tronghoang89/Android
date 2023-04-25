package com.example.hoang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.hoang.Adapter.DienThoaiAdpater;
import com.example.hoang.R;
import com.example.hoang.model.SanPhamMoi;
import com.example.hoang.retrofit.ApiBanHang;
import com.example.hoang.retrofit.RetrofitClient;
import com.example.hoang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    int page=1;
    int loai;
    DienThoaiAdpater adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager layoutManager;
    Handler handler =new Handler();
    boolean isLoading =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai=getIntent().getIntExtra("loai",1);
        AnhXa();
        ActionToolBar();
        getData(page);
        addEvenLoad();
    }

    private void addEvenLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading ==false ){
                    if (layoutManager.findLastCompletelyVisibleItemPosition()==sanPhamMoiList.size()-1){
                        isLoading=true;
                        loadMore();
                    }
                }
            }

        });
    }
    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
             sanPhamMoiList.add(null);
             adapterDt.notifyItemChanged(sanPhamMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //renover null
                sanPhamMoiList.remove(sanPhamMoiList.size()-1);
                adapterDt.notifyItemChanged(sanPhamMoiList.size());
                page=page+1;
                getData(page);
                adapterDt.notifyDataSetChanged();
                isLoading=false;
            }
        },2000);
    }

    // Lay du lieu tu mysql ve
    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                if (adapterDt==null){
                                    sanPhamMoiList=sanPhamMoiModel.getResult();
                                    adapterDt=new DienThoaiAdpater(getApplicationContext(),sanPhamMoiList);
                                    recyclerView.setAdapter(adapterDt);
                                }else {
                                    int vitri=sanPhamMoiList.size()-1;
                                    int sladd=sanPhamMoiModel.getResult().size();
                                    for (int i=0;i<sladd;i++){
                                        sanPhamMoiList.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    adapterDt.notifyItemRangeChanged(vitri,sladd);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "Du lieu da het", Toast.LENGTH_SHORT).show();
                                isLoading=true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi server", Toast.LENGTH_SHORT).show();
                        }
                ));
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




    private void AnhXa() {
        recyclerView  =findViewById(R.id.recycleview_dienthoai);
        toolbar =findViewById(R.id.toobar);
        layoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList=new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}