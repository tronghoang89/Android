package com.admin.hoang.retrofit;

import com.admin.hoang.model.DonHangModel;
import com.admin.hoang.model.LoaiSpModel;
import com.admin.hoang.model.MessageModel;
import com.admin.hoang.model.SanPhamMoiModel;
import com.admin.hoang.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
//GET DATA
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
//POS DATA
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel>getSanPham(
            @Field("page")int page,
            @Field("loai")int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel>dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel>dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel>CreateOrder(
            @Field("email") String email,
            @Field("sodt") String sodt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel>xemdonhang(
            @Field("iduser") int id
    );
    @POST("search.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel>search(
            @Field("search") String search
    );
    @POST("delete.php")
    @FormUrlEncoded
    Observable<MessageModel>xoaSanPham(
            @Field("id") int id
    );
    @POST("themspmoi.php")
    @FormUrlEncoded
    Observable<MessageModel>AddSp(
            @Field("tensp") String ten,
            @Field("giasp") String gia,
            @Field("hinhanh ") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai
    );
    @POST("update.php")
    @FormUrlEncoded
    Observable<MessageModel>updatesp(
            @Field("tensp") String ten,
            @Field("giasp") String gia,
            @Field("hinhanh ") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int idloail,
            @Field("id") int id
    );
    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel>updateToken(
            @Field("id") int id,
            @Field("giasp") String token
            );
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file
    );
}

