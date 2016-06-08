package com.heartblood.heartgit.news;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.heartblood.heartgit.R;
import com.heartblood.heartgit.common.AppActivity;
import com.heartblood.heartgit.common.EndlessRecyclerOnScrollListener;
import com.heartblood.heartgit.common.Global;
import com.heartblood.heartgit.common.adapter.NewsListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by heartblood on 16/5/31.
 */
public class NewsActivity extends AppActivity {

    @BindView(R.id.news_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.news_card_ptr_frame)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.news_card_loading)
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    @BindView(R.id.news_card_loadmore)
    AVLoadingIndicatorView mAVLoadingMoreView;

    private JSONArray mJsonData;
    private NewsListAdapter mNewsListAdapter;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Fresco.initialize(this);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        try {

        } catch (Exception e) {

        }
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            // 下拉加载
            @Override
            public void onLoadMore(int currentPage) {
                mAVLoadingMoreView.setY(mAVLoadingMoreView.getY()+mAVLoadingMoreView.getHeight());
                ViewTranslationY(mAVLoadingMoreView, 80-mAVLoadingMoreView.getHeight());
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(Global.GETSFBLOG+currentPage, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        int count = response.length();
                        for (int i =0 ; i < count; i++)
                            try {
                                mJsonData.put(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                ViewTranslationY(mAVLoadingMoreView, mAVLoadingMoreView.getHeight()+80);
                                adapterUpdate();
                            }
                        }, 1800);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        ViewTranslationY(mAVLoadingMoreView, mAVLoadingMoreView.getHeight()+80);
                        Snackbar.make(mRecyclerView, "没有了哟", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                });
            }
        });
        getNews(Global.GETSFBLOG+"0");
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mRecyclerView.getChildAdapterPosition(mRecyclerView.getChildAt(0)) == 0)
                        return mRecyclerView.getChildAt(0).getTop() >= 0;
                return false;
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                newsUpdate();
            }
        });
    }

    /**
     *
     * view Y轴移动
     * @param view
     * @param toTranslationY
     */
    private void ViewTranslationY(final View view, float toTranslationY) {
        if (ViewHelper.getTranslationY(view) == toTranslationY) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(view), toTranslationY).setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(view, translationY);
            }
        });
        animator.start();
    }

    /**
     * 新闻初始化
     */
    private void newsInit() {
        mNewsListAdapter = new NewsListAdapter(this, mJsonData);
        mRecyclerView.setAdapter(mNewsListAdapter);
    }

    /**
     * 新闻更新
     */
    private void newsUpdate() {
        mEndlessRecyclerOnScrollListener.init();
        getNews(Global.GETSFBLOG+"0");
    }

    /**
     * 适配器更新
     */
    private void adapterUpdate() {
        mNewsListAdapter.notifyItemInserted(mJsonData.length()-1);
    }

    /**
     * 获取新闻
     * @param url restful地址
     */
    private void getNews(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                mJsonData = response;
                newsInit();
                mPtrFrameLayout.refreshComplete();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // TODO: 16/5/9 获取json失败
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("测试", "报错报错啦");
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

}
