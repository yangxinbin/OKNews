package com.baihui.yangxb.oknews.entity;

import java.util.List;

/**
 * Created by yangxb on 17-11-29.
 */

public class ToutiaoLoopnewsBean {

    /**
     * reason : 请求成功
     * result : {"list":[{"id":"wechat_20171129010212","title":"关于麻醉你需要了解的常识！","source":"麻醉博物馆","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-37624653.static/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129010212"},{"id":"wechat_20171129006751","title":"花光2亿，拖垮2个亿万富翁，交往5个鲜肉，最拜金女星她数第一！","source":"娱乐圈那点事","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448153.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129006751"},{"id":"wechat_20171129006725","title":"张靓颖现身活动，豁出去了，网友：裙子短的我都不好意思看了！","source":"娱乐圈那点事","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448108.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129006725"},{"id":"wechat_20171129007117","title":"飒飒秋雨中，浅浅石溜泻 | 古诗赏析","source":"古诗词赏析","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448597.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129007117"}],"totalPage":52096,"ps":4,"pno":1}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * list : [{"id":"wechat_20171129010212","title":"关于麻醉你需要了解的常识！","source":"麻醉博物馆","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-37624653.static/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129010212"},{"id":"wechat_20171129006751","title":"花光2亿，拖垮2个亿万富翁，交往5个鲜肉，最拜金女星她数第一！","source":"娱乐圈那点事","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448153.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129006751"},{"id":"wechat_20171129006725","title":"张靓颖现身活动，豁出去了，网友：裙子短的我都不好意思看了！","source":"娱乐圈那点事","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448108.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129006725"},{"id":"wechat_20171129007117","title":"飒飒秋雨中，浅浅石溜泻 | 古诗赏析","source":"古诗词赏析","firstImg":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-60448597.jpg/640","mark":"","url":"http://v.juhe.cn/weixin/redirect?wid=wechat_20171129007117"}]
         * totalPage : 52096
         * ps : 4
         * pno : 1
         */

        private int totalPage;
        private int ps;
        private int pno;
        private List<ListBean> list;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }

        public int getPno() {
            return pno;
        }

        public void setPno(int pno) {
            this.pno = pno;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : wechat_20171129010212
             * title : 关于麻醉你需要了解的常识！
             * source : 麻醉博物馆
             * firstImg : http://zxpic.gtimg.com/infonew/0/wechat_pics_-37624653.static/640
             * mark :
             * url : http://v.juhe.cn/weixin/redirect?wid=wechat_20171129010212
             */

            private String id;
            private String title;
            private String source;
            private String firstImg;
            private String mark;
            private String url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getFirstImg() {
                return firstImg;
            }

            public void setFirstImg(String firstImg) {
                this.firstImg = firstImg;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
