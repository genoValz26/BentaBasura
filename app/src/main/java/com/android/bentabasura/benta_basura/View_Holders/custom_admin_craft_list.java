package com.android.bentabasura.benta_basura.View_Holders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.Pages.Admin_ManageUsers;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 12/29/2017.
 */

public class custom_admin_craft_list extends BaseAdapter {

    private Context ctx;
    private ArrayList<Craft> craft;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_admin_craft_list(Context context, ArrayList<Craft>  craft) {
        this.ctx = context;
        this.craft =  craft;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return  craft.size();
    }

    public void addItem(Craft craftItem) {
        craft.add( craftItem);
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

        View rowData = inflater.inflate(R.layout.admin_craft_list_feed, null);
        ImageView imgThumbCraft = (ImageView) rowData.findViewById(R.id.imgThumbCraft);
        TextView UploadedDate = (TextView) rowData.findViewById(R.id.UploadedDate);
        TextView CraftName = (TextView) rowData.findViewById(R.id.CraftName);
        TextView CraftDetail = (TextView) rowData.findViewById(R.id.CraftDetail);
        Button btnDelete = (Button) rowData.findViewById(R.id.btnDelete);

        Picasso.with(ctx).load(craft.get(position).getImageUrl()).placeholder(R.drawable.progress_animation).fit().into(imgThumbCraft);
        UploadedDate.setText(craft.get(position).getUploadedDate());
        CraftName.setText(craft.get(position).getCraftName());
        CraftDetail.setText(craft.get(position).getCraftDescription());


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detailsIntent = new Intent(parent.getContext(), Admin_ManageUsers.class);

                final AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("Delete Craft");
                builder.setMessage("Are you sure you want to Delete this?.");

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(craft.get(position).getCraftCategory()).child(craft.get(position).getCraftID()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                parent.getContext().startActivity(detailsIntent);

                            }
                        });
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog  alertDialog = builder.create();
                alertDialog.show();
            }
        });


        return rowData;
    }
}