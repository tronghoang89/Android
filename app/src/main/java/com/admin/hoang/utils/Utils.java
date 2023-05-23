package com.admin.hoang.utils;

import com.admin.hoang.model.Giohang;
import com.admin.hoang.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.81.1/banhang/";
    public static List<Giohang>gioHangs;
    public static List<Giohang>muahangs=new ArrayList<>();
    public static User user_current =new User();
}
