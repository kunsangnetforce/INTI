package com.netforceinfotech.inti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

public class LoginActivity extends AppCompatActivity {

    LinearLayout linearLayoutLanguage;
    ImageView imageViewLogo;
    Context context;
    CoordinatorLayout coordinateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    private void initView() {
        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        linearLayoutLanguage = (LinearLayout) findViewById(R.id.linearLayoutLanguage);
        setupLanguageDropDown(linearLayoutLanguage);
        imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        Glide.with(context).fromResource()
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 100)).load(R.drawable.logo).into(imageViewLogo);

    }

    private void setupLanguageDropDown(LinearLayout linearLayoutLanguage) {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(LoginActivity.this, linearLayoutLanguage);

// Add normal items (text only)
        droppyBuilder.addMenuItem(new DroppyMenuItem("English"))
                .addMenuItem(new DroppyMenuItem("Spain"));

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                showMessage("position: " + id + " clicked");
            }
        });

        DroppyMenuPopup droppyMenu = droppyBuilder.build();
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
