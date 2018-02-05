package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.Tips;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.Pages.Admin_EditTips;
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
    private ArrayList<Tips> orig;

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
                detailsIntent = new Intent(parent.getContext(), Admin_EditTips.class);
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
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Tips> results = new ArrayList<Tips>();
                if (orig == null)
                    orig = tips;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Tips g : orig) {
                            if (g.getTipsTitle().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                tips = (ArrayList<Tips>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}