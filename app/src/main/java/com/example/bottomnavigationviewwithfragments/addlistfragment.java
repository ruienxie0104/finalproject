package com.example.bottomnavigationviewwithfragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.util.Calendar;


public class addlistfragment extends Fragment {
    
    String TAG = "addlistfragment";
    EditText illustrate_text;
    ImageButton save_list_btn;
    Spinner class_spn, item_spn;
    Button scanqrcode;
    String str_spnclass, str_spnitem;
    TextView spinner_result;
    private String mystr;
    private TextView qrcode_result;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    int caseNum;
    private String userName;
    private String userId;
    public static String caseDevice;
    public static String caseRoom;
    private String caseReason;
    private String casekey;
    private String[] classitem;
    private String[] deviceitem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        caseNum = 0;
        View view = inflater.inflate(R.layout.fragment_addlistfragment, container, false);
        save_list_btn = view.findViewById(R.id.save_list_btn);
        class_spn = view.findViewById(R.id.class_spn);
        item_spn = view.findViewById(R.id.item_spn);
        qrcode_result = view.findViewById(R.id.textView_qrcoderesult);
      //  spinner_result = view.findViewById(R.id.textView_spinnerresult);

        illustrate_text = view.findViewById(R.id.illustrate_text);
        scanqrcode = view.findViewById(R.id.scanqrcode);
        class_spn = view.findViewById(R.id.class_spn);
        class_spn.setOnItemSelectedListener(spnOnclassSelected);
        item_spn = view.findViewById(R.id.item_spn);
        item_spn.setOnItemSelectedListener(spnOnItemSelected);
        classitem = getResources().getStringArray(R.array.classroom_list);
        deviceitem = getResources().getStringArray(R.array.item_list);
        if (caseRoom == null){
            caseRoom = "606";
        }
        if (caseDevice == null){
            caseDevice = "麥克風";
        }



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Account");

        scanqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartForResult.launch(new Intent(getActivity(), scanqrcodeActivity.class));
            }
        });

        save_list_btn.setOnClickListener(onClick);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        readCaseNum();
        readUserInfo();
    }

    private AdapterView.OnItemSelectedListener spnOnItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            str_spnitem = String.valueOf(adapterView.getSelectedItemId());
            Log.d(TAG, "onItemSelected: str_spnitem = "+deviceitem[i]);
            caseDevice = deviceitem[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private AdapterView.OnItemSelectedListener spnOnclassSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            str_spnclass = String.valueOf(adapterView.getSelectedItemId());
            Log.d(TAG, "onItemSelected: str_spnclass = "+classitem[i]);
            caseRoom = classitem[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String[] arr;
                        // Handle the Intent
                        if (data != null) {
                            mystr = data.getStringExtra("result");
                            arr = mystr.split(" ");
                            str_spnclass = arr[0];
                            caseRoom = arr[0];
                            Log.d(TAG, "onActivityResult: str_spnclass = "+str_spnclass);
                            str_spnitem = arr[1];
                            caseDevice = arr[1];
                            Log.d(TAG, "onActivityResult: str_spnitem = "+str_spnitem);
                        }

                        qrcode_result.setText(mystr);
                    }
                }
            });
    
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.save_list_btn:
                    Log.d(TAG, "onClick: save_list_btn");
                    Calendar calendar = Calendar.getInstance();
                    long unixTime = calendar.getTimeInMillis();
                    Log.d(TAG,"calendar--->>>"+"當前時間為：" + calendar.get(Calendar.YEAR) +
                                    "年 " + calendar.get(Calendar.MONTH) +
                                    "月 " + calendar.get(Calendar.DAY_OF_MONTH) +
                                    "日 " + calendar.get(Calendar.HOUR_OF_DAY) +
                                    "時 " + calendar.get(Calendar.MINUTE) +
                                    "分 " + calendar.get(Calendar.SECOND) +
                                    "秒");
                    int month = calendar.get(Calendar.MONTH)+1;
                    String Time = ""+calendar.get(Calendar.YEAR)+"年"+month+"月"+
                            calendar.get(Calendar.DAY_OF_MONTH) +"日"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "時"+calendar.get(Calendar.MINUTE)+"分";
                    Log.d(TAG, "onClick: 時間 = "+Time);

                    if (illustrate_text.equals(null)){
                        caseReason = "";
                    }else {
                        caseReason = illustrate_text.getText().toString();
                    }
                    casekey = "Case"+caseNum;

                    User user = new User(userName, userId,Time,
                            caseRoom,caseDevice,caseReason,casekey);
                    Log.d(TAG, "onClick: now Add caseNum = "+caseNum);
                    myRef.child("Case"+caseNum).setValue(user);
                    caseNum++;
                    Log.d(TAG, "onClick: now Add caseNum after = "+caseNum);
                    illustrate_text.setText("");
                    class_spn.setSelection(0);
                    item_spn.setSelection(0);
                    qrcode_result.setText("");
                    Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                    break;
            }
            
        }
    };

    private void readCaseNum(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                caseNum = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: "+snapshot.child("username").toString());
                    caseNum++;
                }
                Log.d(TAG, "onDataChange: dataSnapshot.getChildren() ="+ dataSnapshot.getChildren());
                Log.d(TAG, "onDataChange: caseNum = "+caseNum);
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

