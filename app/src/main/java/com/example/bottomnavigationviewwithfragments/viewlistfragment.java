package com.example.bottomnavigationviewwithfragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Query;

import org.reactivestreams.FlowAdapters;

import java.util.ArrayList;
import java.util.List;


public class viewlistfragment extends Fragment {

    private String TAG = "viewlistfragment";
    //ListView show_all, show_self;
    Switch change;
    TextView tvname, tvid, tvitem, tvreason, tvtime;
    RecyclerView recyclerView_userlist;
    LinearLayoutManager userListLayoutManager;
    UserListAdapter adapter;
    private List<User> mUserList= new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String userName;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_viewlistfragment, container, false);
        //show_all = view1.findViewById(R.id.View_all);
        //show_self = view1.findViewById(R.id.View_self);
        recyclerView_userlist = view1.findViewById(R.id.recyclerView_userlist);
        change = view1.findViewById(R.id.switch1);
        tvname = view1.findViewById(R.id.text_username);
        tvid = view1.findViewById(R.id.text_userid);
        tvitem = view1.findViewById(R.id.text_fixreitem);
        tvreason = view1.findViewById(R.id.text_fixreason);
        tvtime = view1.findViewById(R.id.text_fixtime);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Account");
        userListLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_userlist.setLayoutManager(userListLayoutManager);
        adapter = new UserListAdapter(mUserList,getContext());
        recyclerView_userlist.setAdapter(adapter);
        readAllCase();

        adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "onClick: position = "+position);
                tvname.setText(mUserList.get(position).getUsername());
                tvid.setText(mUserList.get(position).getUserid());
                tvtime.setText(mUserList.get(position).getTime());
                tvitem.setText(mUserList.get(position).getDevice());
                tvreason.setText(mUserList.get(position).getReason());
            }
        });

        adapter.setOnItemLongClickListener(new UserListAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "onItemLongClick: long position = "+position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("刪除項目");
                dialog.setMessage("確認刪除項目");
                dialog.setCancelable(true);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: OK");
                        myRef.child(mUserList.get(position).getKey()).removeValue();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "onClick: 取消");
                    }
                });
                dialog.show();
            }
        });

        change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Log.d(TAG, "onCheckedChanged: true");
                    readOnlyCurrentUserCase();

                }else {
                    Log.d(TAG, "onCheckedChanged: false");
                    readAllCase();
                }
            }
        });

        return view1;
    }

    @Override
    public void onStart() {
        super.onStart();
        readUserInfo();
    }

    private void readAllCase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+snapshot.child("username"));
                    Log.d(TAG, "onDataChange: "+snapshot.child("userid"));
                    Log.d(TAG, "onDataChange: snapshot.getKey() = "+snapshot.getKey());
                    User user = new User(snapshot.child("username").getValue().toString(),
                            snapshot.child("userid").getValue().toString(),
                            snapshot.child("time").getValue().toString(),
                            snapshot.child("room").getValue().toString(),
                            snapshot.child("device").getValue().toString(),
                            snapshot.child("reason").getValue().toString(),
                            snapshot.getKey().toString());
                    mUserList.add(user);
                }
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: dataSnapshot.getChildren() ="+ dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readOnlyCurrentUserCase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+snapshot.child("username"));
                    Log.d(TAG, "onDataChange: "+snapshot.child("userid"));
                    Log.d(TAG, "onDataChange: snapshot.getKey() = "+snapshot.getKey());
                    if (snapshot.child("userid").getValue().toString().equals(userId)){
                        User user = new User(snapshot.child("username").getValue().toString(),
                                snapshot.child("userid").getValue().toString(),
                                snapshot.child("time").getValue().toString(),
                                snapshot.child("room").getValue().toString(),
                                snapshot.child("device").getValue().toString(),
                                snapshot.child("reason").getValue().toString(),
                                snapshot.getKey().toString());
                        mUserList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onDataChange: dataSnapshot.getChildren() ="+ dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void readUserInfo(){
        ref.child(auth.getCurrentUser().getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: name = "+snapshot.getValue().toString());
                userName = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.child(auth.getCurrentUser().getUid()).child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: id = "+snapshot.getValue().toString());
                userId = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}