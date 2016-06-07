package com.heartblood.heartgit.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.heartblood.heartgit.R;
import com.heartblood.heartgit.common.AppActivity;
import com.heartblood.heartgit.common.adapter.NewsListAdapter;
import com.heartblood.heartgit.common.utils.HttpJsonParse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
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
    private JSONArray mJsonData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Fresco.initialize(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getNews("http://119.29.58.43/api/getSfBlog/getPage=1", this);
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
    private void newsUpdate() {
        getNews("http://119.29.58.43/api/getSfBlog/getPage=0", this);
    }
    private void getNews(String url, final NewsActivity mContext) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                mJsonData = response;
                mRecyclerView.setAdapter(new NewsListAdapter(mContext, mJsonData));
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
