package com.example.bottomnavigationviewwithfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class personalfragment extends Fragment {
    EditText edname, edemail, edstudentid;
    private FirebaseAuth auth;
    private DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalfragment, container, false);
        edname = view.findViewById(R.id.editTextTextPersonName);
        edemail = view.findViewById(R.id.editTextTextPersonemail);
        edemail.setFocusable(false);
        edemail.setFocusableInTouchMode(false);
        edstudentid = view.findViewById(R.id.editTextTexstudentid);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Account");
        edemail.setText(auth.getCurrentUser().getEmail());
        ref.child(auth.getCurrentUser().getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edname.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(auth.getCurrentUser().getUid()).child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edstudentid.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton imageButton = view.findViewById(R.id.menu2_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu2 = new PopupMenu(getActivity(), imageButton);
                popupMenu2.getMenu().add("登出");
                popupMenu2.show();
                popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle() == "登出") {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            // finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        // Inflate the layout for this fragment

        Button btsavesetting = view.findViewById(R.id.buttonprofile);
        btsavesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString();
                String id = edstudentid.getText().toString();
                String key = ref.child(auth.getCurrentUser().getUid()).getKey();
                post post_test = new post(id, name);
                Map<String, Object> postValues = post_test.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);
                ref.updateChildren(childUpdates);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

}