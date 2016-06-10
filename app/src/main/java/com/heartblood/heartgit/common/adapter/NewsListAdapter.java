package com.heartblood.heartgit.common.adapter;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.heartblood.heartgit.R;
import com.heartblood.heartgit.common.Global;
import com.heartblood.heartgit.news.NewsActivity;
import com.heartblood.heartgit.news.NewsDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by heartblood on 16/5/31.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private NewsActivity mContext;
    private LayoutInflater mLayoutInflater;
    private JSONArray mDatalist;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public NewsListAdapter(NewsActivity mContext, JSONArray mDataList) {
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDatalist = mDataList;
        this.mContext = mContext;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mLayoutInflater.inflate(R.layout.fragment_news_card, parent, false);
        view.setOnClickListener(this);

        return new NewsItemHolder(view);
    }
    /**
     * bind holder data
     * @param holder bind view holder
     * @param position data list position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: 16/5/9 bind holder data
        try {
            JSONObject mdataObject = mDatalist.getJSONObject(position);
            NewsItemHolder newsHolder = (NewsItemHolder) holder;
            newsHolder.newsTitle.setText(mdataObject.getString("title"));
            newsHolder.newsAuthor.setText(mdataObject.getString("author"));
            Uri uri = Uri.parse(mdataObject.getString("authorhref"));
            newsHolder.draweeView.setImageURI(uri);
            holder.itemView.setTag(mdataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        // TODO: 16/5/9 return holder data item count
        return mDatalist.length();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(JSONObject)v.getTag());
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private static class NewsItemHolder extends RecyclerView.ViewHolder{
        public TextView newsTitle;
        public TextView newsAuthor;
        public SimpleDraweeView draweeView;
        public NewsItemHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.news_card_label_title);
            newsAuthor = (TextView) itemView.findViewById(R.id.news_card_label_author);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.news_card_portrait);
        }
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , JSONObject data);
    }
}
