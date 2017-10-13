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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gd185082 on 10/6/2017.
 */

public class custom_craftlist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Craft> craft;
    private static LayoutInflater inflater = null;
    Intent  detailsIntent;

    public custom_craftlist(Context context, ArrayList<Craft> craft)
    {
        this.ctx = context;
        this.craft = craft;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return craft.size();
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

        View rowData = inflater.inflate(R.layout.custom_craftlist, null);

        ImageView imgThumbTrash = (ImageView) rowData.findViewById(R.id.imgThumbTrash);
        TextView txtTrashDate = (TextView) rowData.findViewById(R.id.txtTrashDate);
        TextView txtTrashName = (TextView) rowData.findViewById(R.id.txtTrashName);
        TextView txtTrashDescription = (TextView) rowData.findViewById(R.id.txtTrashDescription);
        TextView txtSellerInfo  = (TextView) rowData.findViewById(R.id.txtSellerInfo);
        Button   btnReadMore = (Button) rowData.findViewById(R.id.btnReadMore);

        Picasso.with(ctx).load(craft.get(position).getImageUrl()).fit().into(imgThumbTrash);
        txtTrashName.setText(craft.get(position).getCraftName());
        txtTrashDate.setText(craft.get(position).getUploadedDate());
        txtTrashDescription.setText(craft.get(position).getCraftDescription());
        txtSellerInfo.setText(craft.get(position).getSellerContact());

        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                detailsIntent = new Intent(parent.getContext(), BuyRawDetails.class);

                detailsIntent.putExtra("CraftName", craft.get(position).getCraftName());
                detailsIntent.putExtra("CraftPic", craft.get(position).getImageUrl());
                detailsIntent.putExtra("CraftDescription",craft.get(position).getCraftDescription());
                detailsIntent.putExtra("CraftQuantity", craft.get(position).getCraftQuantity());
                detailsIntent.putExtra("CraftCategory", craft.get(position).getCraftCategory());
                detailsIntent.putExtra("CraftPrice", craft.get(position).getCraftPrice());
                detailsIntent.putExtra("CraftSeller", craft.get(position).getSellerContact());

                parent.getContext().startActivity(detailsIntent);
            }
        });



        return rowData;
    }
}
