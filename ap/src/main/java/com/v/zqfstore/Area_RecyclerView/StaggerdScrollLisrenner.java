package com.v.zqfstore.Area_RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Administrator on 2017/12/8.
 */

public class StaggerdScrollLisrenner extends RecyclerView.OnScrollListener {
    StaggeredGridLayoutManager manager;
    boolean isSlidingToLast = false;
    public StaggerdScrollLisrenner(StaggeredGridLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            //获取最后一个完全显示的ItemPosition
            int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
            int lastVisiblePos = getMaxElem(lastVisiblePositions);
            int totalItemCount = manager.getItemCount();

            // 判断是否滚动到底部
            if (lastVisiblePos == (totalItemCount -1) && isSlidingToLast) {
                //加载更多功能的代码
                if(loadMoreData!=null){
                    loadMoreData.loadMore();
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //用来判断横向滑动方向，dy用来判断纵向滑动方向
        if(dy > 0){
            //大于0表示，正在向下滚动
            isSlidingToLast = true;
        }else{
            //小于等于0 表示停止或向上滚动
            isSlidingToLast = false;
        }
    }
    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i]>maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
    LoadMoreListener loadMoreData;

    public void setLoadMoreData(LoadMoreListener loadMoreData) {
        this.loadMoreData = loadMoreData;
    }

    public interface LoadMoreListener{
        void loadMore();
    }
}
