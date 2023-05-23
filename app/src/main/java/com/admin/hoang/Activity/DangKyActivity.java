package com.admin.hoang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.hoang.R;
import com.admin.hoang.retrofit.ApiBanHang;
import com.admin.hoang.retrofit.RetrofitClient;
import com.admin.hoang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText edUsername, edPassword, edEmail, edComfirm,edmobile;
    Button btn;
    TextView textView;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();

    }

    private void initControl() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKy();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),DangNhapActivity.class);
                startActivity(intent);
            }
        });

    }

    private void dangKy() {
        String str_email=edEmail.getText().toString().trim();
        String str_pass=edPassword.getText().toString().trim();
        String str_username=edUsername.getText().toString().trim();
        String str_comfirm=edComfirm.getText().toString().trim();
        String str_mobile=edmobile.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Ban chua nhap email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(), "Ban chua nhap ten", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Ban chua nhap pass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_comfirm)){
            Toast.makeText(getApplicationContext(), "Ban chua comfirm pass", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(), "Ban chua nhap dien thoai", Toast.LENGTH_SHORT).show();
        }else {
            if (str_comfirm.equals(str_pass)){
             firebaseAuth =FirebaseAuth.getInstance();
             firebaseAuth.createUserWithEmailAndPassword(str_email,str_pass)
                     .addOnCompleteListener(DangKyActivity.this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                if (user !=null){
                                    posData(str_username,str_pass, str_email, str_mobile,user.getUid());
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Email da co", Toast.LENGTH_SHORT).show();
                            }
                         }
                     });

            }
            else {
                Toast.makeText(getApplicationContext(), "Mat khau khong hop le", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void posData(String str_email,String str_pass,String str_username,String str_mobile,String uid){
        compositeDisposable.add(apiBanHang.dangki(str_email,str_pass,str_username,str_mobile,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPass(str_pass);
                                Intent intent =new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(getApplicationContext(),userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void initView() {
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edEmail=findViewById(R.id.editTextEmaildangky);
        edPassword=findViewById(R.id.editTextRegisterPass);
        edUsername=findViewById(R.id.editTextRegisterName);
        edComfirm=findViewById(R.id.txtconfirmpass);
        edmobile=findViewById(R.id.txtmoblie);
        btn =findViewById(R.id.Btn_Register);
        textView=findViewById(R.id.editTextExistingUser);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}