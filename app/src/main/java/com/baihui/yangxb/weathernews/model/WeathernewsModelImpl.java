package com.baihui.yangxb.weathernews.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.baihui.yangxb.tools.OkHttpUtils;
import com.baihui.yangxb.tools.Urls;
import com.baihui.yangxb.weathernews.entity.WeathernewsBean;
import com.baihui.yangxb.weathernews.utils.WeatherJsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class WeathernewsModelImpl implements WeathernewsModel {
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    @Override
    public void loadWeatherData(String cityName, final LoadWeatherListener listener) {
        try {
            String url = Urls.WEATHER_HOST + Urls.WEATHER_CITYNAME + URLEncoder.encode(cityName, "utf-8") + Urls.WEATHER_END;
            Log.v("jjjjjjjjjj","--------weatherurl-----"+url);
            OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    List<WeathernewsBean> lists = WeatherJsonUtils.getWeatherInfo(response);
                    listener.onSuccess(lists);
                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure("load weather data failure.", e);
                }
            };
            OkHttpUtils.get(url, callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadLocation(Context context, final LoadLocationListener listener) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                listener.onFailure("location failure.", null);
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            //经度
            latitude = location.getLatitude();
            //纬度
            longitude = location.getLongitude();
        }
        String url = getLocationURL(latitude, longitude);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                String city = WeatherJsonUtils.getCity(response);
                if (TextUtils.isEmpty(city)) {
                    listener.onFailure("load location info failure.", null);
                } else {
                    listener.onSuccess(city);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load location info failure.", e);
            }
        };
        OkHttpUtils.get(url, callback);

    }

    private String getLocationURL(double latitude, double longitude) {
        StringBuffer sb = new StringBuffer(Urls.INTERFACE_LOCATION);
        sb.append(longitude).append(",").append(latitude).append(Urls.INTERFACE_LOCATION_END);
        return sb.toString();
    }

    public interface LoadWeatherListener {
        void onSuccess(List<WeathernewsBean> list);

        void onFailure(String msg, Exception e);
    }

    public interface LoadLocationListener {
        void onSuccess(String cityName);

        void onFailure(String msg, Exception e);
    }

}
