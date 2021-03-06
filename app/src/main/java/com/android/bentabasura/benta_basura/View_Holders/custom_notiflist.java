package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.Notification;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.Utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ccs on 10/19/17.
 */

public class custom_notiflist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Notification> notifs;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_notiflist(Context context, ArrayList<Notification> notifs)
    {
        this.ctx = context;
        this.notifs = notifs ;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notifs.size();
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

        View rowData = inflater.inflate(R.layout.row_notif, null);

        ImageView imgThumbTrash = (ImageView) rowData.findViewById(R.id.imgPicNotif);
        TextView txtContent = (TextView) rowData.findViewById(R.id.txtContent);
        TextView txtDate = (TextView) rowData.findViewById(R.id.txtDateNotif);

        Picasso.with(ctx).load(notifs.get(position).getNotifByPic()).placeholder(R.drawable.progress_animation).transform(new RoundedTransformation(100, 20)).fit().into(imgThumbTrash);
        txtContent.setText(notifs.get(position).getNotifMessage());
        txtDate.setText(notifs.get(position).getNotifDate());

        return rowData;
    }

}
