package com.baihui.yangxb.mainpage.presenter;

import com.baihui.yangxb.R;
import com.baihui.yangxb.mainpage.view.MainpageView;

/**
 * Created by Administrator on 2016/11/25 0025.
 * MainpagePresenter的实现类
 */

public class MainpagePresenterImpl implements MainpagePresenter {
    private MainpageView mainpageView;

    public MainpagePresenterImpl(MainpageView mainpageView) {
        this.mainpageView = mainpageView;
    }

    @Override
    public void switchNavigation(int id) {
        switch (id){
            case R.id.nav_baihuinews:
                mainpageView.selectBaihuinews();
                break;
    /*        case R.id.nav_authormanager:
                mainpageView.selectAuthormanage();
                break;
            case R.id.nav_meipaivideo:
                mainpageView.selectMeipaivideo();
                break;
            case R.id.nav_comicnews:
                mainpageView.selectComicnews();
                break;
            case R.id.nav_baihuimap:
                mainpageView.selectBaihuimap();
                break;
            case R.id.nav_weathernews:
                mainpageView.selectWeathernews();
                break;
            case R.id.nav_enjoygame:
                mainpageView.selectEnjoygame();
                break;
            case R.id.nav_userhelp:
                mainpageView.selectUserhelp();
                break;
            case R.id.nav_aboutauthor:
                mainpageView.selectAboutauthor();
                break; */
            default:
                mainpageView.selectBaihuinews();
                break;

        }

    }
}
