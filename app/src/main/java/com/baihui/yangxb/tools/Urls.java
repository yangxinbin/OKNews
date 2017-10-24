package com.baihui.yangxb.tools;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class Urls {
    //http://v.juhe.cn/toutiao/index?type=top&key=22b714e5c6a52c651a1fd640e47e6383 //百汇新闻
    public static final String HOST = "http://v.juhe.cn/toutiao/index";
    public static final String END_URL = "&key=22b714e5c6a52c651a1fd640e47e6383";

    public static final String TYPE = "?type=";
    public static final String HOST_TYPE =HOST+TYPE;

    public static final String TOP_ID = "top";
    public static final String SHEHUI_ID = "shehui";
    public static final String GUOMEI_ID = "guonei";
    public static final String GUOJI_ID = "guoji";
    public static final String YULE_ID = "yule";
    public static final String TIYU_ID = "tiyu";
    public static final String JUNSHI_ID = "junshi";
    public static final String KEJI_ID = "keji";
    public static final String CAIJING_ID = "caijing";
    public static final String SHISHANG_ID = "shishang";

    //http://op.juhe.cn/onebox/weather/query?cityname=%E6%B7%B1%E5%9C%B3&dtype=&key=3198ddb3e2701c280f50612cf02a01d7 //天气预报
    public static final String WEATHER_HOST = "http://op.juhe.cn/onebox/weather/query";
    public static final String WEATHER_CITYNAME = "?cityname=";
    public static final String WEATHER_END = "&dtype=&key=3198ddb3e2701c280f50612cf02a01d7";

    /* http://api.map.baidu.com/geocoder/v2/?ak=SauFB1KDFiV9Q75Uc06kBGqDGMBNUyuY&mcode=F1:FF:CC:5C:76:5D:5A:C3:D5:F4:50:86:E5:AD:7C:06:49:06:E0:27;
     com.yxb.baihui.baihui.weathernews.model&callback=renderReverse&location=39.984578,116.451345&output=json&pois=1    *///定位
    public static final String INTERFACE_LOCATION = "http://api.map.baidu.com/geocoder/v2/?ak=SauFB1KDFiV9Q75Uc06kBGqDGMBNUyuY&mcode=F1:FF:CC:5C:76:5D:5A:C3:D5:F4:50:86:E5:AD:7C:06:49:06:E0:27;\n" +
            "     com.baihui.yangxb.weathernews.model&callback=renderReverse&location=";
    public static final String INTERFACE_LOCATION_END = "&output=json&pois=1";


}
