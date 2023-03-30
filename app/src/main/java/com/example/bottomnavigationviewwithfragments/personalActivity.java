package com.example.bottomnavigationviewwithfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class personalActivity extends AppCompatActivity {

    private static final String TAG_PERSONALFRAGMENT="personalfragment";

    personalfragment Personalfragment =new personalfragment();

    @NonNull
    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    ImageButton menuBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        menuBtn = findViewById(R.id.menu2_btn);

        menuBtn.setOnClickListener((v)->showMenu());
    }
    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(personalActivity.this, menuBtn);
        popupMenu.getMenu().add("登出");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="登出"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(personalActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

}