package com.baihui.yangxb.oknews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.squareup.picasso.Picasso;


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
    private boolean mShowFooter = true;

    public Boolean isFooter(Boolean isfooter) {
        return this.mShowFooter = isfooter;
    }

    public void setmDate(List<ToutiaonewsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
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
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        if (holder instanceof ItemViewHolder) {

            ToutiaonewsBean news = mData.get(position);
            if (news == null) {
                return;
            }
            if (((ItemViewHolder) holder) != null && ((ItemViewHolder) holder).mTitle != null) {
                ((ItemViewHolder) holder).mTitle.setText(news.getResult().getData().get(position).getTitle());
                if (news.getResult().getData().get(position).getThumbnail_pic_s().isEmpty()) {
                    ((ItemViewHolder) holder).mNewsImg.setImageResource(R.drawable.defaultimage);
                } else {
                    Picasso.with(context).load(news.getResult().getData().get(position).getThumbnail_pic_s()).into(((ItemViewHolder) holder).mNewsImg);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }
        return mData.size() + begin;
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

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

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.item_news_title);
            mNewsImg = (ImageView) v.findViewById(R.id.item_news_img);
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
