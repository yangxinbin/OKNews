package com.baihui.yangxb.weathernews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.weathernews.entity.WeathernewsBean;
import com.baihui.yangxb.weathernews.presenter.WeathernewsPresenter;
import com.baihui.yangxb.weathernews.presenter.WeathernewsPresenterImpl;
import com.baihui.yangxb.weathernews.utils.TempControlView;
import com.baihui.yangxb.weathernews.view.WeathernewsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class WeathernewsFragment extends Fragment implements WeathernewsView {

    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.city)
    TextView city;
    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.todayweek)
    TextView todayweek;
    @Bind(R.id.weatherImage)
    ImageView weatherImage;
    @Bind(R.id.wind)
    TextView wind;
    @Bind(R.id.windpower)
    TextView windpower;
    @Bind(R.id.weather)
    TextView weather;
    @Bind(R.id.temp_control)
    TempControlView tempControl;
    @Bind(R.id.weather_content)
    LinearLayout weatherContent;
    @Bind(R.id.weather_layout)
    LinearLayout weatherLayout;
    @Bind(R.id.root_layout)
    FrameLayout rootLayout;
    private WeathernewsPresenter mWeatherPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherPresenter = new WeathernewsPresenterImpl(getActivity().getApplication(), this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        mWeatherPresenter.loadWeatherData();
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null){
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showWeatherLayout() {
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCity(String city) {
        if (this.city != null) {
            this.city.setText(city);
        }
    }

    @Override
    public void setToday(String data) {
        today.setText(data);
    }

    private String nowtime;

    @Override
    public void setTime(String time) {
        nowtime = time;
    }

    @Override
    public void setTemperature(String temperature) {
        tempControl.setTemp(10, 30, Integer.parseInt(temperature), "更新于" + nowtime);

    }

    @Override
    public void setWind(String wind) {
        this.wind.setText(wind);

    }

    @Override
    public void setWindPower(String windpower) {
        this.windpower.setText(windpower);
    }

    @Override
    public void setWeather(String weather) {
        this.weather.setText(weather);

    }

    @Override
    public void setWeek(String week) {
        this.todayweek.setText("星期" + week);
    }


    @Override
    public void setWeatherImage(String res) {
        weatherImage.setImageResource(getImagID(res));
    }

    @Override
    public void setfutureWeatherData(List<WeathernewsBean> lists) {
        List<View> adapterList = new ArrayList<View>();

        List<WeathernewsBean.ResultBean.DataBean.WeatherBeanX> listfuture = lists.get(0).getResult().getData().getWeather();
        for (int i = 1;i < listfuture.size();i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_weather, null, false);
            TextView dateTV = (TextView) view.findViewById(R.id.date);
            ImageView todayWeatherImage = (ImageView) view.findViewById(R.id.weatherImage);
            TextView todayTemperatureTV = (TextView) view.findViewById(R.id.weatherTemp);
            TextView todayWindTV = (TextView) view.findViewById(R.id.wind);
            TextView todayWeatherTV = (TextView) view.findViewById(R.id.weather);

            dateTV.setText("星期"+listfuture.get(i).getWeek());
            todayTemperatureTV.setText(listfuture.get(i).getInfo().getDay().get(2)+"°C");
            Log.v("jjjjjjj","-----------"+listfuture.get(i).getInfo().getDay().get(4)+"======"+listfuture.get(i).getInfo().getDay().get(1));
            todayWindTV.setText(listfuture.get(i).getInfo().getDay().get(4));
            todayWeatherTV.setText(listfuture.get(i).getInfo().getDay().get(1));
            todayWeatherImage.setImageResource(getImagID(listfuture.get(i).getInfo().getDay().get(1)));
            weatherContent.addView(view);
            adapterList.add(view);
        }

    }

    @Override
    public void showErrorToast(String msg) {
        View view = getActivity() == null ? rootLayout.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        if (isAdded()) {
            final Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
            View snackbarview = snackbar.getView();
            snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
            TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbarText.setTextColor(Color.WHITE);
//            snackbar.setAction("click", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    snackbar.dismiss();
//                }
//            });
            snackbar.show();
            //adapter.isShowFooter(false);//关闭加载更多... 字符串
        }
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private int getImagID(String weather) {
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
    }

}
