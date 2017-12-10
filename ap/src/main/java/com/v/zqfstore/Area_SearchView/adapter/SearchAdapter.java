package com.v.zqfstore.Area_SearchView.adapter;

import android.content.Context;

import com.v.zqfstore.Area_SearchView.model.Bean;
import com.v.zqfstore.Area_SearchView.util.CommonAdapter;
import com.v.zqfstore.Area_SearchView.util.ViewHolder;
import com.v.zqfstore.R;

import java.util.List;


public class SearchAdapter extends CommonAdapter<Bean> {

    public SearchAdapter(Context context, List<Bean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent())
                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());
    }
}
