package com.baihui.yangxb.weathernews.view;



import com.baihui.yangxb.weathernews.entity.WeathernewsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface WeathernewsView {

    void showProgress();
    void hideProgress();
    void showWeatherLayout();

    void setCity(String city);
    void setToday(String data);
    void setTime(String time);
    void setTemperature(String temperature);
    void setWind(String wind);
    void setWindPower(String windpower);
    void setWeather(String weather);
    void setWeek(String week);
    void setWeatherImage(String res);
    void setfutureWeatherData(List<WeathernewsBean> lists);

    void showErrorToast(String msg);

}
