package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 1/7/2018.
 */

public class home_tips_list extends BaseAdapter {

    private Context ctx;
    private ArrayList<Tips> tips;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public home_tips_list(Context context, ArrayList<Tips> tips) {
        this.ctx = context;
        this.tips = tips;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tips.size();
    }

    public void addItem(Tips tipsItem) {
        tips.add(tipsItem);
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

        View rowData = inflater.inflate(R.layout.tips_content, null);
        ImageView imgThumbTips = (ImageView) rowData.findViewById(R.id.imgThumbTips);
        TextView tipsDate = (TextView) rowData.findViewById(R.id.tipsDate);
        TextView tipsTitle = (TextView) rowData.findViewById(R.id.tipsTitle);
        TextView tipsDetail = (TextView) rowData.findViewById(R.id.tipsDetail);

        Picasso.with(ctx).load(tips.get(position).getTipsImagepath()).placeholder(R.drawable.progress_animation).fit().into(imgThumbTips);
        tipsTitle.setText(tips.get(position).getTipsTitle());
        tipsDate.setText(tips.get(position).getTipsDate());
        tipsDetail.setText(tips.get(position).getTipsDetail());

        return rowData;
    }
}