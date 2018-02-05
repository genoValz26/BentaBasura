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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.Trash;
import com.android.bentabasura.benta_basura.Models.Users;
import com.android.bentabasura.benta_basura.Pages.Admin_ManageUsers;
import com.android.bentabasura.benta_basura.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private ArrayList<Users> orig;

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

                final AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("Delete User");
                builder.setMessage("Are you sure you want to Delete this?.");

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(users.get(position).getUserid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Users> results = new ArrayList<Users>();
                if (orig == null)
                    orig = users;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Users g : orig) {
                            if (g.getFullname().toLowerCase()
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
                users = (ArrayList<Users>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}