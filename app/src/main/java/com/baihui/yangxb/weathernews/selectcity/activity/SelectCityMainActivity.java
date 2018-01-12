package com.baihui.yangxb.weathernews.selectcity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baihui.yangxb.R;
import com.baihui.yangxb.weathernews.selectcity.adapter.CityListAdapter;
import com.baihui.yangxb.weathernews.selectcity.adapter.ResultListAdapter;
import com.baihui.yangxb.weathernews.selectcity.bean.City;
import com.baihui.yangxb.weathernews.selectcity.bean.LocateState;
import com.baihui.yangxb.weathernews.selectcity.db.DBManager;
import com.baihui.yangxb.weathernews.selectcity.utils.StringUtils;
import com.baihui.yangxb.weathernews.selectcity.view.SlideBar;

import java.util.List;



public class SelectCityMainActivity extends AppCompatActivity implements View.OnClickListener {



    private ListView mListview;
    private ListView mResultListView;
    private SlideBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ImageView backBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;
    private LinearLayout activitymain;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocation();
        initData();
        initView();
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.start();
    }
    private void initData() {
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }

            @Override
            public void onLocateClick() {
                Log.e("onLocateClick", "重新定位...");
                Log.v("yxbbbb","------------onLocateClick FAILED");
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                mLocationClient.start();
            }
        });

        mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        activitymain = (LinearLayout) findViewById(R.id.activity_main);//为了监听软键盘弹出与隐藏
        mListview = (ListView) findViewById(R.id.listview_all_city);
        mListview.setAdapter(mCityAdapter);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SlideBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SlideBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListview.setSelection(position);
            }
        });
        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        searchBox = (EditText) findViewById(R.id.et_search);
        /*监听软键盘弹出与隐藏*/
        activitymain.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
                    mLetterBar.setVisibility(View.GONE);
                }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
                    mLetterBar.setVisibility(View.VISIBLE);
                }
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        backBtn = (ImageView) findViewById(R.id.back);

        clearBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
      //  if((v.getId() == R.id.layout_locate) && f){
      // }
        switch (v.getId()) {
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void back(String city) {
     ///Toast.makeText(this,city,Toast.LENGTH_LONG).show();
        Log.v("yxbbb","-------back----"+city);
        Intent i = new Intent();
        if (city == null){
            return;
        }
        i.putExtra("CityName",city);
        setResult(RESULT_OK,i);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.v("yxbbbb","-----Error----"+BDLocation.TypeServerError);//yxb
            if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                //定位失败
                mCityAdapter.updateLocateState(LocateState.FAILED, null);
            }
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //String addr = bdLocation.getAddrStr();    //获取详细地址信息
            //String country = bdLocation.getCountry();    //获取国家
            //String province = bdLocation.getProvince();    //获取省份
            //获取城市
            city = bdLocation.getCity();
            Log.v("yxbbb","--------"+city);
            String district = bdLocation.getDistrict();    //获取区县
            //String street = bdLocation.getStreet();    //获取街道信息
            String locationcity = StringUtils.extractLocation(city, district);
            mCityAdapter.updateLocateState(LocateState.SUCCESS, locationcity);
        }
    }
}
