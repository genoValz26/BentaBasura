package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.News;
import com.android.bentabasura.benta_basura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 1/7/2018.
 */

public class home_news_list extends BaseAdapter {

    private Context ctx;
    private ArrayList<News> news;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public home_news_list(Context context, ArrayList<News> news) {
        this.ctx = context;
        this.news = news;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return news.size();
    }

    public void addItem(News newsItem) {
        news.add(newsItem);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowData = inflater.inflate(R.layout.news_content, null);
        ImageView imgThumbTips = (ImageView) rowData.findViewById(R.id.imgThumbNews);
        TextView newsDate = (TextView) rowData.findViewById(R.id.newsDate);
        TextView newsTitle = (TextView) rowData.findViewById(R.id.newsTitle);
        TextView newsDetail = (TextView) rowData.findViewById(R.id.newsDetail);

        Picasso.with(ctx).load(news.get(position).getNewsImagepath()).placeholder(R.drawable.progress_animation).fit().into(imgThumbTips);
        newsTitle.setText(news.get(position).getNewsTitle());
        newsDate.setText(news.get(position).getNewsDate());
        newsDetail.setText(news.get(position).getNewsDetail());


        return rowData;
    }
}
