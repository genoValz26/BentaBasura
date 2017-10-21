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
 * Created by gd185082 on 10/6/2017.
 */

public class custom_trashlist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Trash> trash;
    private static LayoutInflater inflater = null;
    Intent  detailsIntent;

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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowData = inflater.inflate(R.layout.raw_list_feed, null);

        ImageView imgThumbTrash = (ImageView) rowData.findViewById(R.id.imgThumbTrash);
        TextView txtTrashDate = (TextView) rowData.findViewById(R.id.txtTrashDate);
        TextView txtTrashName = (TextView) rowData.findViewById(R.id.txtTrashName);
        TextView txtTrashDescription = (TextView) rowData.findViewById(R.id.txtTrashDescription);
        Button   btnReadMore = (Button) rowData.findViewById(R.id.btnReadMore);
        TextView txtTrashPrice = (TextView) rowData.findViewById(R.id.txtTrashPrice);

        Picasso.with(ctx).load(trash.get(position).getImageUrl()).placeholder( R.drawable.progress_animation ).fit().into(imgThumbTrash);
        txtTrashName.setText(trash.get(position).getTrashName());
        txtTrashDate.setText(trash.get(position).getUploadedDate());
        txtTrashDescription.setText(trash.get(position).getTrashDescription());
        txtTrashPrice.setText("Php " + trash.get(position).getTrashPrice() + ".00");

        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                detailsIntent = new Intent(parent.getContext(), BuyRawDetails.class);

                detailsIntent.putExtra("TrashName", trash.get(position).getTrashName());
                detailsIntent.putExtra("TrashPic", trash.get(position).getImageUrl());
                detailsIntent.putExtra("TrashDescription", trash.get(position).getTrashDescription());
                detailsIntent.putExtra("TrashQuantity", trash.get(position).getTrashQuantity());
                detailsIntent.putExtra("TrashCategory", trash.get(position).getTrashCategory());
                detailsIntent.putExtra("TrashPrice", trash.get(position).getTrashPrice());
                detailsIntent.putExtra("TrashSeller", trash.get(position).getSellerContact());
                detailsIntent.putExtra("TrashId", trash.get(position).getTrashId());
                detailsIntent.putExtra("UploadedBy", trash.get(position).getUploadedBy());

                parent.getContext().startActivity(detailsIntent);
            }
        });



        return rowData;
    }
}
