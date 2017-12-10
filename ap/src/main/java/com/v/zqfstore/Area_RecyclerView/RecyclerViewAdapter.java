package com.v.zqfstore.Area_RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.v.zqfstore.Glideutils.GlideLoadImageUtil.GlideLoadImageUtil;
import com.v.zqfstore.JavaBean.MenuBean;
import com.v.zqfstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Integer> mHeights;
    private MenuBean bean;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        mHeights = new ArrayList<>();
    }

    public void setBean(MenuBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }

    //创建视图
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    //绑定视图的数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 随机高度, 模拟瀑布效果.
        if (mHeights.size() <= position) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
        ViewGroup.LayoutParams layoutParams = holder.imageitem.getLayoutParams();
        layoutParams.height = mHeights.get(position);
        holder.imageitem.setLayoutParams(layoutParams);

        MenuBean.ApkBean apkBean = bean.getApk().get(position);
        GlideLoadImageUtil.load(context, apkBean.getIconUrl(), holder.imageitem);
        holder.tvFoodName.setText(apkBean.getCategoryName());

    }


    @Override
    public int getItemCount() {
        return (bean == null) ? 0 : bean.getApk().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFoodName;
        private final ImageView imageitem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            imageitem = itemView.findViewById(R.id.imageitem);
        }

    }
}

