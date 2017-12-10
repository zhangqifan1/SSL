package com.v.zqfstore.Area_RecyclerView;

import com.v.zqfstore.JavaBean.MenuBean;

/**
 * Created by Administrator on 2017/12/7.
 */

public interface IModel {
    void RequestData(String url,CallBack callBack);
    interface CallBack{
        void setData(MenuBean bean);
    }
}
