package com.admin.hoang.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.hoang.Adapter.LoaiSpAdapter;
import com.admin.hoang.Adapter.SanPhamMoiAdapter;
import com.admin.hoang.R;
import com.admin.hoang.model.SanPhamMoi;
import com.admin.hoang.model.User;
import com.admin.hoang.retrofit.RetrofitClient;
import com.bumptech.glide.Glide;
import com.admin.hoang.model.LoaiSp;
import com.admin.hoang.retrofit.ApiBanHang;
import com.admin.hoang.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi>mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user")!=null){
            User user=Paper.book().read("user");
            Utils.user_current=user;
        }
        getToken();
        Mapping();
        ActionBar();
        if (isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEvenClick();
        }
        else {
            Toast.makeText(getApplicationContext(), "Khong co internet ,vui long kết nối lại", Toast.LENGTH_SHORT).show();
        }

    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                       if (!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(),s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        messageModel -> {

                                        },
                                            throwable -> {
                                                Log.d("Log",throwable.getMessage());
                                            }
                                    ));
                       }
                    }
                });
    }

    private void getEvenClick() {
       listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i){
                   case 0:
                       Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                       startActivity(trangchu);
                       break;
                   case 1:
                       Intent dienthoai=new Intent(getApplicationContext(),DienThoaiActivity.class);
                       dienthoai.putExtra("loai",1);
                       startActivity(dienthoai);
                       break;
                   case 2:
                       Intent laptop=new Intent(getApplicationContext(),DienThoaiActivity.class);
                       laptop.putExtra("loai",2);
                       startActivity(laptop);
                       break;
                   case 5:
                       Intent donhang=new Intent(getApplicationContext(),XemDonHangActivity.class);
                       donhang.putExtra("loai",2);
                       startActivity(donhang);
                       break;
                   case 6:
                       // xoa ky user
                       Intent quanly=new Intent(getApplicationContext(),QuanLyActivity.class);
                       startActivity(quanly);
                       break;
                   case 7:
                       // xoa ky user
                       Paper.book().delete("user");
                       FirebaseAuth.getInstance().signOut();
                       Intent dangnhap=new Intent(getApplicationContext(),DangNhapActivity.class);
                       startActivity(dangnhap);
                       finish();
                       break;
               }
           }
       });
    }



    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                mangSpMoi =sanPhamMoiModel.getResult();
                                mangloaisp.add(new LoaiSp("Quan Ly",""));
                                mangloaisp.add(new LoaiSp("Dang Xuat"," "));
                                spAdapter =new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewManHinhChinh.setAdapter(spAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong co ket noi vao server dc"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void getLoaiSanPham() {
       compositeDisposable.add(apiBanHang.getLoaiSp()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                       loaiSpModel -> {
                           if (loaiSpModel.isSuccess()){
                               mangloaisp=loaiSpModel.getResult();
                               loaiSpAdapter=new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                               listViewManHinhChinh.setAdapter(loaiSpAdapter);
                           }
                       }
               ));

    }

    private void ActionViewFlipper(){
        List<String>mangquangcao=new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i=0;i<mangquangcao.size();i++){
            ImageView imageView =new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Mapping(){
        imgsearch=findViewById(R.id.imgsearch);
        toolbar =findViewById(R.id.toobarmanhinhchinh);
        viewFlipper=findViewById(R.id.viewflipper);
        recyclerViewManHinhChinh=findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh= findViewById(R.id.listviewmanhinhchinh);
        navigationView =findViewById(R.id.navigationview);
        drawerLayout =findViewById(R.id.drawerlayout);
        badge=findViewById(R.id.menu_sl);
        frameLayout=findViewById(R.id.frameCart);
        // khoi tao apter
        loaiSpAdapter =new LoaiSpAdapter(getApplicationContext(),mangloaisp);
        // khoi tao list
        mangloaisp =new ArrayList<>();
        //khoi tao san pham moi
       // mangloaisp =new ArrayList<>();
        mangSpMoi =new ArrayList<>();
        // tao gia hang
        if (Utils.gioHangs==null){
            Utils.gioHangs=new ArrayList<>();
        }else {
            // so luong mua hang
            int total=0;
            for (int i=0;i<Utils.gioHangs.size();i++){
                total=total+Utils.gioHangs.get(i).getSoluong();
            }
            badge.setText(String.valueOf(total));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang =new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // so luong mua hang
        int total=0;
        for (int i=0;i<Utils.gioHangs.size();i++){
            total=total+Utils.gioHangs.get(i).getSoluong();
        }
        badge.setText(String.valueOf(total));
    }

    private boolean isConnected(Context context){
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}