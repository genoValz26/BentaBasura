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

import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.Pages.Admin_ManageUsers;
import com.android.bentabasura.benta_basura.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Geno on 12/20/2017.
 */

public class custom_userslist extends BaseAdapter {

    private Context ctx;
    private ArrayList<Users> users;
    private static LayoutInflater inflater = null;
    Intent detailsIntent;

    public custom_userslist(Context context, ArrayList<Users>  users) {
        this.ctx = context;
        this.users =  users;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return  users.size();
    }

    public void addItem(Users  usersItem) {
        users.add( usersItem);
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

        View rowData = inflater.inflate(R.layout. users_list_feed, null);
        ImageView imgThumbUsers = (ImageView) rowData.findViewById(R.id.imgThumbUsers);
        TextView fullnametxt = (TextView) rowData.findViewById(R.id.fullnametxt);
        TextView emailtxt = (TextView) rowData.findViewById(R.id.emailtxt);
        TextView mobiletxt = (TextView) rowData.findViewById(R.id.mobiletxt);
        Button btnDelete = (Button) rowData.findViewById(R.id.btnDelete);

        Picasso.with(ctx).load(users.get(position).getprofile_picture()).placeholder(R.drawable.progress_animation).fit().into(imgThumbUsers);
        fullnametxt.setText(users.get(position).getFullname());
        emailtxt.setText(users.get(position).getEmail());
       mobiletxt.setText(users.get(position).getcontact_number());


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsIntent = new Intent(parent.getContext(), Admin_ManageUsers.class);

                parent.getContext().startActivity(detailsIntent);
            }
        });


        return rowData;
    }
}