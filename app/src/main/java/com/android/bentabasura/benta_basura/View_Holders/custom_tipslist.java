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

import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.Pages.BuyCraftedDetails;
import com.android.bentabasura.benta_basura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 12/19/2017.
 */

public class custom_tipslist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Tips> tips;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_tipslist(Context context, ArrayList<Tips> tips) {
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

        View rowData = inflater.inflate(R.layout.tips_list_feed, null);
        ImageView imgThumbTips = (ImageView) rowData.findViewById(R.id.imgThumbTips);
        TextView tipsDate = (TextView) rowData.findViewById(R.id.tipsDate);
        TextView tipsTitle = (TextView) rowData.findViewById(R.id.tipsTitle);
        TextView tipsDetail = (TextView) rowData.findViewById(R.id.tipsDetail);
        Button btnReadMore = (Button) rowData.findViewById(R.id.btnReadMore);

        Picasso.with(ctx).load(tips.get(position).getTipsImagepath()).placeholder(R.drawable.progress_animation).fit().into(imgThumbTips);
       tipsTitle.setText(tips.get(position).getTipsTitle());
        tipsDate.setText(tips.get(position).getTipsDate());
        tipsDetail.setText(tips.get(position).getTipsDetail());


        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsIntent = new Intent(parent.getContext(), BuyCraftedDetails.class);
                detailsIntent.putExtra("TipsTitle", tips.get(position).getTipsTitle());
                detailsIntent.putExtra("TipsDetail", tips.get(position).getTipsDetail());
                detailsIntent.putExtra("TipsDate", tips.get(position).getTipsDate());
                detailsIntent.putExtra("TipsImagePath", tips.get(position).getTipsImagepath());
                detailsIntent.putExtra("TipsID", tips.get(position).getTipsID());
                parent.getContext().startActivity(detailsIntent);
            }
        });


        return rowData;
    }
}