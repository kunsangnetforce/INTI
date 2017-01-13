package com.netforceinfotech.inti.myprofile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netforceinfotech.inti.R;
import com.netforceinfotech.inti.dashboard.DashboardActivity;
import com.netforceinfotech.inti.general.UserSessionManager;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

public class MyProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = this;
        userSessionManager = new UserSessionManager(this);
        setupToolBar(getString(R.string.profile));
        initView();
    }

    private void initView() {

        userSessionManager.checkLogin();



    }



    private void setupToolBar(String title) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageViewSetting = (ImageView) toolbar.findViewById(R.id.imageViewSetting);
        TextView textViewTitle = (TextView) toolbar.findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
        setupSettingMenu(imageViewSetting);
        ImageView imageViewBack = (ImageView) toolbar.findViewById(R.id.imageViewBack);
        imageViewBack.setVisibility(View.GONE);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


    }

    private void setupSettingMenu(ImageView imageView) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imageView);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem(getString(R.string.logout)))
                      .addMenuItem(new DroppyMenuItem(getString(R.string.dashboard)));



// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
              if(id==0){
                  userSessionManager.logoutUser();
                  finish();
              }else if(id==1){
                  Intent intent = new Intent(MyProfileActivity.this, DashboardActivity.class);
                  startActivity(intent);
                  finish();
                  overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
              }
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void showMessage(String s) {

        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }


}
