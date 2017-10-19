package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ccs on 10/19/17.
 */

public class custom_commentlist_crafted extends BaseAdapter {

    private Context ctx;
    private ArrayList<Comment> comments;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_commentlist_crafted(Context context, ArrayList<Comment> comments)
    {
        this.ctx = context;
        this.comments = comments ;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments .size();
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

        View rowData = inflater.inflate(R.layout.row_comment, null);

        ImageView imgThumbCraft = (ImageView) rowData.findViewById(R.id.imgPic);
        TextView txtName = (TextView) rowData.findViewById(R.id.txtName);
        TextView txtDate = (TextView) rowData.findViewById(R.id.txtDate);
        TextView txtComment = (TextView) rowData.findViewById(R.id.txtComment);

        Picasso.with(ctx).load(comments .get(position).getProfileImage()).placeholder(R.drawable.progress_animation).transform(new RoundedTransformation(100, 20)).fit().into(imgThumbCraft);
        txtName.setText(comments .get(position).getProfileName());
        txtDate.setText(comments .get(position).getCommentDate());
        txtComment.setText(comments .get(position).getComment());

        return rowData;
    }

}
