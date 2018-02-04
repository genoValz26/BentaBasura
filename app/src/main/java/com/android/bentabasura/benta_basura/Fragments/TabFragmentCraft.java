package com.android.bentabasura.benta_basura.Fragments;
/**
 * Created by ccs on 10/18/17.
 */

import android.app.ProgressDialog;
import android.app.SearchableInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.bentabasura.benta_basura.Models.ActiveUser;
import com.android.bentabasura.benta_basura.Models.Craft;
import com.android.bentabasura.benta_basura.Pages.Login;
import com.android.bentabasura.benta_basura.R;
import com.android.bentabasura.benta_basura.View_Holders.custom_craftlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabFragmentCraft extends Fragment {

    ListView lstMyCraft;
    ActiveUser activeUser;
    String oldestPostId = "";
    ProgressDialog mProgressDialog;
    DatabaseReference databaseReferenceCraft;
    custom_craftlist customCraftAdapter;
    ArrayList<Craft> craftArray = new ArrayList<>();
    List<String> craftCategory = Arrays.asList("Decoration", "Furniture", "Projects", "Accessories");
    TextView txtEmpty;
    SearchView searchTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_items_craft, container, false);

        //set persist to true
        Login.setPersist(true);

        lstMyCraft = (ListView) view.findViewById(R.id.lstMyCraft);
        mProgressDialog = new ProgressDialog(container.getContext());
        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
        searchTxt = (SearchView) view.findViewById(R.id.searchTxt);

        databaseReferenceCraft  = FirebaseDatabase.getInstance().getReference("Craft");

        activeUser = ActiveUser.getInstance();

        customCraftAdapter = new custom_craftlist(container.getContext(), craftArray);

        lstMyCraft.setAdapter(customCraftAdapter);

        //LoadTrash
        getCraftDataFromFirebase();

        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customCraftAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customCraftAdapter.getFilter().filter(s);
                return false;
            }
        });

        return view;
    }

    private void getCraftDataFromFirebase()
    {
        for(final String trashCat: craftCategory)
        {
            databaseReferenceCraft.child(trashCat.toString()).orderByChild("reverseDate").addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren())
                        {
                            boolean found = false;

                            oldestPostId = postSnapShot.getKey();

                            mProgressDialog.setMessage("Loading...");
                            mProgressDialog.show();


                            for (Craft itemCraft : craftArray)
                            {
                                if (itemCraft.getCraftID().equals(oldestPostId)) {
                                    found = true;
                                    break;
                                }
                            }

                            if (!found)
                            {
                                Craft craft = postSnapShot.getValue(Craft.class);

                                if (craft.getCraftCategory().equals(trashCat.toString()))
                                {
                                    if (craft.getUploadedBy().equals(activeUser.getUserId())) {

                                        craft.setCraftID(postSnapShot.getKey().toString());
                                        craftArray.add(craft);
                                        customCraftAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }

                    if(craftArray.size() == 0){
                        lstMyCraft.setVisibility(View.INVISIBLE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        lstMyCraft.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}