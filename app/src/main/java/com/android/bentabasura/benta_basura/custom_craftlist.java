package com.android.bentabasura.benta_basura;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        this.craft = craft ;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return craft .size();
    }

    public void addItem(Craft craftItem)
    {
        craft.add(craftItem);
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

        View rowData = inflater.inflate(R.layout.craft_list_feed, null);
        ImageView imgThumbCraft = (ImageView) rowData.findViewById(R.id.imgThumbCraft);
        TextView txtCraftDate = (TextView) rowData.findViewById(R.id.txtCraftDate);
        TextView txtCraftName = (TextView) rowData.findViewById(R.id.txtCraftName);
        TextView txtCraftDescription = (TextView) rowData.findViewById(R.id.txtCraftDescription);
        TextView txtCraftPrice = (TextView) rowData.findViewById(R.id.txtCraftPrice);
        Button   btnReadMore = (Button) rowData.findViewById(R.id.btnReadMore);

        Picasso.with(ctx).load(craft .get(position).getImageUrl()).placeholder( R.drawable.progress_animation ).fit().into(imgThumbCraft);
        txtCraftName.setText(craft .get(position).getCraftName());
        txtCraftDate.setText(craft .get(position).getUploadedDate());
        txtCraftDescription.setText(craft .get(position).getCraftDescription());
        txtCraftPrice.setText("Php " + craft.get(position).getCraftPrice() + ".00");

        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                detailsIntent = new Intent(parent.getContext(), BuyCraftedDetails.class);
                detailsIntent.putExtra("CraftName", craft.get(position).getCraftName());
                detailsIntent.putExtra("CraftPic", craft.get(position).getImageUrl());
                detailsIntent.putExtra("CraftDescription",craft.get(position).getCraftDescription());
                detailsIntent.putExtra("CraftQuantity", craft.get(position).getCraftQuantity());
                detailsIntent.putExtra("CraftCategory", craft.get(position).getCraftCategory());
                detailsIntent.putExtra("CraftPrice", craft.get(position).getCraftPrice());
                detailsIntent.putExtra("CraftSeller", craft.get(position).getSellerContact());
                detailsIntent.putExtra("UploadedBy", craft.get(position).getUploadedBy());
                detailsIntent.putExtra("CraftId", craft.get(position).getCraftID());
                parent.getContext().startActivity(detailsIntent);
            }
        });



        return rowData;
    }
}
