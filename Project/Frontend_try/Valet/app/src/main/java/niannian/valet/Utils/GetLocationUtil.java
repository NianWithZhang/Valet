package niannian.valet.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.widget.Toast;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class GetLocationUtil {


//    public Pair<Double,Double> getLocation(Context context){
//        LocationManager lm = (LocationManager) ContextCompat.getSystemService(context,LocationManager.class);//Context.LOCATION_SERVICE);
//
//
//    }

    public static Location getLocatioon(Context context, Activity activity) {
        //获得位置服务
        LocationManager locationManager = (LocationManager) ContextCompat.getSystemService(context,LocationManager.class);//Context.LOCATION_SERVICE);

        boolean ok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!=PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                } else {
                    return getLocation(locationManager,context);//getLocation为定位方法
                }
            } else {
                return getLocation(locationManager,context);//getLocation为定位方法
            }
        } else {
            Toast.makeText(context, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ActivityCompat.startActivityForResult(activity,intent, 1315,new Bundle());
        }

        return null;
    }
    private static Location getLocation(LocationManager locationManager,Context context){
                String provider = judgeProvider(locationManager,context);
        //有位置提供器的情况
        if (provider != null) {
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PERMISSION_GRANTED) {
                return null;
            }
            return locationManager.getLastKnownLocation(provider);
        }else{
            //不存在位置提供器的情况
            Toast.makeText(context,"不存在位置提供器的情况",Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private static String judgeProvider(LocationManager locationManager,Context context) {
        List<String> providerList = locationManager.getProviders(true);
        if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;//网络定位
        }else if(providerList.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;//GPS定位
        }else{
            Toast.makeText(context,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
