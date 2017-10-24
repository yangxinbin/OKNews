package com.baihui.yangxb.weathernews.model;

import android.content.Context;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface WeathernewsModel {
    void loadWeatherData(String cityName, WeathernewsModelImpl.LoadWeatherListener listener);

    void loadLocation(Context context, WeathernewsModelImpl.LoadLocationListener listener);
}
