package com.v.zqfstore.Area_RecyclerView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.v.zqfstore.JavaBean.MenuBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/8.
 */

public class IModelImpl implements IModel {
    @Override
    public void RequestData(String url, final CallBack callBack) {
        OkHttpUtils.get().url(url).build().execute(new Callback<MenuBean>() {
            @Override
            public MenuBean parseNetworkResponse(Response response) throws IOException {
                String string = response.body().string();
                MenuBean menuBean = new Gson().fromJson(string, MenuBean.class);
                return menuBean;
            }

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(MenuBean response) {
                callBack.setData(response);
            }
        });
    }
}
