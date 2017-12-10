package com.v.zqfstore.Area_RecyclerView;

import com.v.zqfstore.JavaBean.MenuBean;

/**
 * Created by Administrator on 2017/12/8.
 */

public class Presenter_Recycle {
    private IModel iModel;
    private IView iView;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public Presenter_Recycle(IView iView) {
        this.iView = iView;
        iModel=new IModelImpl();
    }

    public void setModelWithView(){
        iModel.RequestData(url, new IModel.CallBack() {
            @Override
            public void setData(MenuBean bean) {
                iView.setBean(bean);
            }
        });

    }
}
