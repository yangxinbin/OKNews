package com.baihui.yangxb.oknews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public class ToutiaonewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private OnItemnewsClickListener mOnItemnewsClickListener;//自注册的接口给调用者用于点击逻辑
    private List<ToutiaonewsBean> mData;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public static final int TYPE_HEADER = 2;
    private boolean mShowFooter = true;
    private boolean mShowHeader = true;
    private View mHeaderView;

/*    public Boolean isFooter(Boolean isfooter) {
        return this.mShowFooter = isfooter;
    }*/

    public void setmDate(List<ToutiaonewsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }
    public void reMove(){
        List<ToutiaonewsBean> m = new ArrayList<ToutiaonewsBean>();
        this.mData = m;

        this.notifyDataSetChanged();
    }

    public void setHeaderView(View headerView) {//add header
        mHeaderView = headerView;
    }

    /**
     * 添加列表项     * @param item
     */
    public void addItem(ToutiaonewsBean bean) {
        mData.add(bean);
        this.notifyDataSetChanged();
    }

    public ToutiaonewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter && !mShowHeader) {
            return TYPE_ITEM;
        }
        if (position == 0) {
            return TYPE_HEADER;//add header
        }
        if (position + 1 == getItemCount() || mHeaderView == null) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {//add header
            return new ItemViewHolder(mHeaderView);
        }
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;//add header
        final int pos = getRealPosition(holder);
        Log.v("yxb", "----pos------"+pos);

        if (holder instanceof ItemViewHolder) {
            ToutiaonewsBean news = mData.get(pos);//add header
            if (news == null) {
                return;
            }
            if (((ItemViewHolder) holder) != null && ((ItemViewHolder) holder).mTitle != null) {
                ((ItemViewHolder) holder).mTitle.setText(news.getResult().getData().get(pos).getTitle());
                ((ItemViewHolder) holder).mnew_from.setText("来自："+news.getResult().getData().get(pos).getAuthor_name());
                ((ItemViewHolder) holder).mnew_time.setText("时间："+news.getResult().getData().get(pos).getDate());
                if (news.getResult().getData().get(pos).getThumbnail_pic_s().isEmpty()) {
                    ((ItemViewHolder) holder).mNewsImg.setImageResource(R.drawable.defaultimage);
                } else {
                    Picasso.with(context).load(news.getResult().getData().get(pos).getThumbnail_pic_s()).into(((ItemViewHolder) holder).mNewsImg);
                }
            }
        }
    }
    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }
    @Override
    public int getItemCount() {
        int isFooter = mShowFooter ? 1 : 0;
        int isHeader = mShowHeader ? 1 : 0;

        if (mData == null) {
            return isFooter + isHeader;
        }
        return mData.size() + isFooter + isHeader;
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public void isShowHeader(boolean showHeader) {
        this.mShowHeader = showHeader;
    }

/*    public boolean isShowHeader() {
        return this.mShowHeader;
    }*/

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public ToutiaonewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void setOnItemnewsClickListener(OnItemnewsClickListener onItemnewsClickListener) {
        this.mOnItemnewsClickListener = onItemnewsClickListener;
    }

    public interface OnItemnewsClickListener {
        public void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle;
        public ImageView mNewsImg;
        public TextView mnew_from;
        public TextView mnew_time;

        public ItemViewHolder(View v) {
            super(v);
            if(v == mHeaderView)
                return;
            mTitle = (TextView) v.findViewById(R.id.item_news_title);
            mNewsImg = (ImageView) v.findViewById(R.id.item_news_img);
            mnew_from = (TextView) v.findViewById(R.id.new_from);
            mnew_time = (TextView) v.findViewById(R.id.new_time);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemnewsClickListener != null) {
                mOnItemnewsClickListener.onItemClick(view, this.getLayoutPosition());
            }
        }
    }
}
