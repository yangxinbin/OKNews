package com.baihui.yangxb.weathernews.utils;

import android.text.TextUtils;

import com.baihui.yangxb.tools.JsonUtils;
import com.baihui.yangxb.weathernews.entity.WeathernewsBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Author : lauren
 * Email  : lauren.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 2015/12/22
 */
public class WeatherJsonUtils {

    /**
     * 从定位的json字串中获取城市
     *
     * @param json
     * @return
     */
    public static String getCity(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        JsonElement status = jsonObj.get("status");
        if (status != null && "OK".equals(status.getAsString())) {
            JsonObject result = jsonObj.getAsJsonObject("result");
            if (result != null) {
                JsonObject addressComponent = result.getAsJsonObject("addressComponent");
                if (addressComponent != null) {
                    JsonElement cityElement = addressComponent.get("city");
                    if (cityElement != null) {
                        return cityElement.getAsString().replace("市", "");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取天气信息
     *
     * @param json
     * @return
     */
    public static List<WeathernewsBean> getWeatherInfo(String json) {
        List<WeathernewsBean> list = new ArrayList<WeathernewsBean>();
        if (TextUtils.isEmpty(json)) {
            return list;
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        String status = jsonObj.get("reason").getAsString();
        if ("查询成功!".equals(status) && list != null) {
            WeathernewsBean WeathernewsBean = getWeatherBeanFromJson(jsonObj);
            list.add(WeathernewsBean);
        }
        return list;
    }

    private static WeathernewsBean getWeatherBeanFromJson(JsonObject jsonObject) {
/*        Log.v("jjjjjjjj", "-------------getWeatherBeanFromJson-----");
        *//*json对象们*//*
        JsonObject resultjsonObject = jsonObject.getAsJsonObject("result");
        JsonObject datajsonObject = resultjsonObject.getAsJsonObject("data");
        JsonObject realtimejsonObject = datajsonObject.getAsJsonObject("realtime");
        JsonObject weatherjsonObject = realtimejsonObject.getAsJsonObject("weather");
        JsonObject windjsonObject = realtimejsonObject.getAsJsonObject("wind");

        /*//*具体字段*//**//*
        String date = realtimejsonObject.get("date").getAsString();//日期
        String temperature = weatherjsonObject.get("temperature").getAsString();//当前温度
        String weather = weatherjsonObject.get("info").getAsString();//天气情况
        String week = realtimejsonObject.get("week").getAsString(); //星期几
        String wind = windjsonObject.get("direct").getAsString();
        String windpower = windjsonObject.get("power").getAsString();
        WeathernewsBean WeathernewsBean = new WeathernewsBean();
        *//*设置bean数据*//*
        if (WeathernewsBean.getResult() != null && WeathernewsBean.getResult().getData() != null &&
                WeathernewsBean.getResult().getData().getRealtime() != null && WeathernewsBean.getResult().getData().getRealtime().getWeather() != null) {
            Log.v("jjjjjjjj", "-------util_onSuccess-----"+WeathernewsBean.getResult().getData().getRealtime().getDate());
            WeathernewsBean.getResult().getData().getRealtime().setDate(date);//日期
        Log.v("jjjjjjjj", "-------util_onSuccess---1--"+WeathernewsBean.getResult());
        Log.v("jjjjjjjj", "-------util_onSuccess---2--"+WeathernewsBean.getResult().getData());
        Log.v("jjjjjjjj", "-------util_onSuccess---3--"+WeathernewsBean.getResult().getData().getRealtime());
        Log.v("jjjjjjjj", "-------util_onSuccess---4--"+WeathernewsBean.getResult().getData().getRealtime().getDate());
            WeathernewsBean.getResult().getData().getRealtime().getWeather().setTemperature(temperature);//当前温度
            WeathernewsBean.getResult().getData().getRealtime().getWeather().setInfo(weather);//天气情况
            WeathernewsBean.getResult().getData().getWeather().get(0).setWeek(week);//星期几
            WeathernewsBean.getResult().getData().getRealtime().getWind().setDirect(wind);//风向
            WeathernewsBean.getResult().getData().getRealtime().getWind().setPower(windpower);//风力
            WeathernewsBean.getResult().getData().getRealtime().getWeather().setImg(weather);//天气情况图片
        }*/
        WeathernewsBean weathernewsBean = JsonUtils.deserialize(jsonObject, WeathernewsBean.class);
        return weathernewsBean;
    }


/*    public static int getWeatherImage(String weather) {
        if (weather.equals("多云") || weather.equals("多云转阴") || weather.equals("多云转晴")) {
            return R.drawable.weather_duoyun;
        } else if (weather.equals("中雨") || weather.equals("中到大雨")) {
            return R.drawable.weather_zhongyu;
        } else if (weather.equals("雷阵雨")) {
            return R.drawable.weather_leizhenyu;
        } else if (weather.equals("阵雨") || weather.equals("阵雨转多云")) {
            return R.drawable.weather_zhenyu;
        } else if (weather.equals("暴雪")) {
            return R.drawable.weather_baoxue;
        } else if (weather.equals("暴雨")) {
            return R.drawable.weather_baoyu;
        } else if (weather.equals("大暴雨")) {
            return R.drawable.weather_dabaoyu;
        } else if (weather.equals("大雪")) {
            return R.drawable.weather_daxue;
        } else if (weather.equals("大雨") || weather.equals("大雨转中雨")) {
            return R.drawable.weather_dabaoyu;
        } else if (weather.equals("晴")) {
            return R.drawable.weather_qing;
        } else if (weather.equals("小雪")) {
            return R.drawable.weather_xiaoxue;
        } else if (weather.equals("小雨")) {
            return R.drawable.weather_xiaoyu;
        } else if (weather.equals("阴")) {
            return R.drawable.weather_yin;
        } else if (weather.equals("雨夹雪")) {
            return R.drawable.weather_yujiaxue;
        } else if (weather.equals("阵雪")) {
            return R.drawable.weather_zhenxue;
        } else if (weather.equals("中雪")) {
            return R.drawable.weather_zhongxue;
        } else {
            return R.drawable.weather_duoyun;
        }
    }*/

}
