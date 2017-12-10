package com.v.zqfstore;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.v.zqfstore.Area_RecyclerView.IView;
import com.v.zqfstore.Area_RecyclerView.MyItemDecoration;
import com.v.zqfstore.Area_RecyclerView.Presenter_Recycle;
import com.v.zqfstore.Area_RecyclerView.RecyclerViewAdapter;
import com.v.zqfstore.Area_SearchView.adapter.SearchAdapter;
import com.v.zqfstore.Area_SearchView.model.Bean;
import com.v.zqfstore.Area_SearchView.widge.SearchView;
import com.v.zqfstore.BaseActivities.BaseActivity;
import com.v.zqfstore.JavaBean.MenuBean;
import com.v.zqfstore.JavaBean.MessageBean;
import com.v.zqfstore.JavaBean.WeatherBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class MainActivity extends BaseActivity implements SearchView.SearchViewListener, IView {
    private String path = "http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg";
    private TextView tvLocation;
    private TextView tvWeather;
    /**
     * 搜索结果列表view
     */
    private ListView lvResults;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    private List<Bean> dbData;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<Bean> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;
    private RecyclerView rv;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private RecyclerViewAdapter viewAdapter;
    private NestedScrollView scrollview;
    private MenuBean menuBean1;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        MainActivity.hintSize = hintSize;
    }

    private int a = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        rv.setNestedScrollingEnabled(false);
        final Presenter_Recycle presenter_recycle = new Presenter_Recycle(this);

        presenter_recycle.setUrl(Const.MenuURL);
        presenter_recycle.setModelWithView();

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        viewAdapter = new RecyclerViewAdapter(this);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setAdapter(viewAdapter);
        rv.addItemDecoration(new MyItemDecoration(this));
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.lvTips.setVisibility(View.GONE);
                // 判断 scrollView 当前滚动位置在顶部
                if(scrollview.getScrollY() == 0){
                }

                // 判断scrollview 滑动到底部
                // scrollY 的值和子view的高度一样，这人物滑动到了底部
                if (scrollview.getChildAt(0).getHeight() - scrollview.getHeight()
                        == scrollview.getScrollY()){
                    for (int i = 0; i < 6; i++) {
                        Toast.makeText(MainActivity.this, "已加载", Toast.LENGTH_SHORT).show();
                        menuBean1.getApk().add(menuBean1.getApk().get(i));
                        viewAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });




        EventBus.getDefault().register(this);
//        System.out.println(getCacheDir() + "/" + GlideConfig.GLIDE_CARCH_DIR);

    }

    @Subscribe(sticky = true)
    public void receiveMessage(MessageBean messageBean) {
        if (messageBean != null) {
            String locationInfo = messageBean.getLocationInfo();
            tvLocation.setText(locationInfo);
            String cityName = messageBean.getCityName();
            byte[] bytes;
            try {
                bytes = cityName.getBytes("utf-8");
                String s = new String(bytes);
                OkHttpUtils.get().url(Const.WeaTherBase + s + Const.WeaTherEnd).build().execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        WeatherBean weatherBean = new Gson().fromJson(response, WeatherBean.class);
                        tvWeather.setText(weatherBean.getResult().getToday().getWeather());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bytes = null;
            }
        }


    }

    @Override
    public void getLocation() {
        super.getLocation();
    }

    private void initView() {
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvWeather = (TextView) findViewById(R.id.tvWeather);
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

        rv = (RecyclerView) findViewById(R.id.rv);

        scrollview = (NestedScrollView) findViewById(R.id.scrollview);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    //设置顶部颜色
    @Override
    public void setColor(int color) {
        color = R.color.HuiC;
        super.setColor(color);
    }

    public void tvToGetLocation(View view) {
        getLocation();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    /**
     * 获取db 数据
     */
    private void getDbData() {
        int size = 100;
        dbData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            dbData.add(new Bean(R.drawable.ic_launcher_background, "角色扮演" + (i + 1), "Android自定义view——自定义搜索view", i * 20 + 2 + ""));
        }

    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<>(hintSize);
        for (int i = 1; i <= hintSize; i++) {
            hintData.add("角色扮演" + i);
        }
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getTitle().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i).getTitle());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getTitle().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {

        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        int totalHeightofListView = getTotalHeightofListView(lvResults);
        lvResults.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, totalHeightofListView));

        Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void setBean(MenuBean menuBean) {
        menuBean1 = menuBean;
        if (menuBean1 != null) {
            viewAdapter.setBean(menuBean1);
        }


    }

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
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
}
