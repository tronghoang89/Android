package com.admin.hoang.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.admin.hoang.R;
import com.admin.hoang.databinding.ActivityThemSpBinding;
import com.admin.hoang.model.MessageModel;
import com.admin.hoang.model.SanPhamMoi;
import com.admin.hoang.retrofit.ApiBanHang;
import com.admin.hoang.retrofit.RetrofitClient;
import com.admin.hoang.utils.Utils;
import com.airbnb.lottie.L;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import soup.neumorphism.NeumorphCardView;

public class ThemSpActivity extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    String mediaPath;
    ActivityThemSpBinding binding;
    SanPhamMoi sanPhamSua;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityThemSpBinding.inflate(getLayoutInflater());
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent=getIntent();
        sanPhamSua= (SanPhamMoi) intent.getSerializableExtra("Sua");
        if (sanPhamSua==null){
            // them san pham
            flag=false;
        }
        else {
            //sua san pham
            flag=true;
            binding.btnthemspmoi.setText("Sua san pham");
            //show data
            binding.motaspmoi.setText(sanPhamSua.getMota());
            binding.giaspmoi.setText(sanPhamSua.getGiasp());
            binding.tenspmoi.setText(sanPhamSua.getTensp());
            binding.hinhanhspmoi.setText(sanPhamSua.getHinhanh());
            binding.inputLoaisp.setSelection(sanPhamSua.getLoai());

        }


    }

    private void initData() {
        List<String>strings=new ArrayList<>();
        strings.add("vui long chon loai");
        strings.add("loai 1");
        strings.add("loai 2");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,strings);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnthemspmoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag==true){
                    themsp();
                }
                else {
                    Suasanpham();



                }

            }
        });
        binding.loadanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ThemSpActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void Suasanpham() {
        String str_ten=binding.tenspmoi.getText().toString().trim();
        String str_gia =binding.giaspmoi.getText().toString().trim();
        String str_mota=binding.motaspmoi.getText().toString().trim();
        String str_hinhanh=binding.hinhanhspmoi.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_hinhanh)||loai==0){
            Toast.makeText(getApplicationContext(), "Vui long nhap du thong tin", Toast.LENGTH_SHORT).show();
        }
        else {
            compositeDisposable.add(apiBanHang.updatesp(str_ten,str_gia,str_hinhanh,str_mota,loai,sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath=data.getDataString();
        uploadMultipleFiles();
        Log.d(" Log","onActivityResult :"+ mediaPath);
    }

    private void themsp() {
        String str_ten=binding.tenspmoi.getText().toString().trim();
        String str_gia =binding.giaspmoi.getText().toString().trim();
        String str_mota=binding.motaspmoi.getText().toString().trim();
        String str_hinhanh=binding.hinhanhspmoi.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_hinhanh)||loai==0){
            Toast.makeText(getApplicationContext(), "Vui long nhap du thong tin", Toast.LENGTH_SHORT).show();
        }
        else {
            compositeDisposable.add(apiBanHang.AddSp(str_ten,str_gia,str_hinhanh,str_mota,loai)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
        }
    }
    // lay dong dan
    private String getPath(Uri uri){
        String result;
        Cursor cursor= getContentResolver().query(uri,null,null,null,null);
        if (cursor==null){
            result=uri.getPath();
        }else {
            cursor.moveToFirst();
            int index=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result=cursor.getString(index);
            cursor.close();
        }
        return result;
    }
    // Uploading Image/Video
    private void uploadMultipleFiles() {
        Uri uri=Uri.parse(mediaPath);
        File file=new File(getPath(uri));
        // Map is used to multipart the file using okhttp3.RequestBody
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call < MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, retrofit2.Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.hinhanhspmoi.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("log",t.getMessage());
            }
        } );
    }

    private void initView() {
        spinner=findViewById(R.id.input_loaisp);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}