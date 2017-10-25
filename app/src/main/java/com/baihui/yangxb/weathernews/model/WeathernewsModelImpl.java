package com.baihui.yangxb.weathernews.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
        listener.onSuccess("");
        listener.onFailure("",null);
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
