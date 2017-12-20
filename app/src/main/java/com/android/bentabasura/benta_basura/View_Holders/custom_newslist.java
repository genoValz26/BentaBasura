package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.News;
import com.android.bentabasura.benta_basura.Pages.BuyCraftedDetails;
import com.android.bentabasura.benta_basura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 12/20/2017.
 */

public class custom_newslist extends BaseAdapter {

    private Context ctx;
    private ArrayList<News> news;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_newslist(Context context, ArrayList<News> news) {
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

        View rowData = inflater.inflate(R.layout.news_list_feed, null);
        ImageView imgThumbTips = (ImageView) rowData.findViewById(R.id.imgThumbNews);
        TextView newsDate = (TextView) rowData.findViewById(R.id.newsDate);
        TextView newsTitle = (TextView) rowData.findViewById(R.id.newsTitle);
        TextView newsDetail = (TextView) rowData.findViewById(R.id.newsDetail);
        Button btnReadMore = (Button) rowData.findViewById(R.id.btnReadMore);

        Picasso.with(ctx).load(news.get(position).getNewsImagepath()).placeholder(R.drawable.progress_animation).fit().into(imgThumbTips);
        newsTitle.setText(news.get(position).getNewsTitle());
        newsDate.setText(news.get(position).getNewsDate());
        newsDetail.setText(news.get(position).getNewsDetail());


        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsIntent = new Intent(parent.getContext(), BuyCraftedDetails.class);
                detailsIntent.putExtra("NewsTitle", news.get(position).getNewsTitle());
                detailsIntent.putExtra("NewsDetail", news.get(position).getNewsDetail());
                detailsIntent.putExtra("NewsDate", news.get(position).getNewsDate());
                detailsIntent.putExtra("NewsImagePath", news.get(position).getNewsImagepath());
                detailsIntent.putExtra("NewsID", news.get(position).getNewsID());
                parent.getContext().startActivity(detailsIntent);
            }
        });


        return rowData;
    }
}
