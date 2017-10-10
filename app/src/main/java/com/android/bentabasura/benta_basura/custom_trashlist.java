package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by reymond on 10/10/2017.
 */

public class custom_trashlist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Trash> trash;
    private static LayoutInflater inflater = null;

    public custom_trashlist(Context context, ArrayList<Trash> trash)
    {
        this.ctx = context;
        this.trash = trash;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return trash.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowData = inflater.inflate(R.layout.custom_craftlist, null);

        ImageView imgThumbTrash = (ImageView) rowData.findViewById(R.id.imgThumbTrash);
        TextView txtTrashDate = (TextView) rowData.findViewById(R.id.txtTrashDate);
        TextView txtTrashName = (TextView) rowData.findViewById(R.id.txtTrashName);
        TextView txtTrashDescription = (TextView) rowData.findViewById(R.id.txtTrashDescription);
        TextView txtSellerInfo  = (TextView) rowData.findViewById(R.id.txtSellerInfo);

        Picasso.with(ctx).load(trash.get(position).getImageUrl()).fit().into(imgThumbTrash);
        txtTrashName.setText(trash.get(position).getTrashName());
        txtTrashDate.setText(trash.get(position).getUploadedDate());
        txtTrashDescription.setText(trash.get(position).getTrashDescription());
        txtSellerInfo.setText(trash.get(position).getSellerContact());

        return rowData;
    }
}
