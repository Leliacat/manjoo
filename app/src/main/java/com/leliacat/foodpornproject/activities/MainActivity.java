package com.leliacat.foodpornproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.leliacat.foodpornproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goTakePic(View view)
    {
        Intent goToTakePic = new Intent(this,TakePic.class);
        startActivity(goToTakePic);
    }

    public void goGallery(View view)
    {
        Intent goToGallery = new Intent(this,Gallery.class);
        startActivity(goToGallery);
    }

    public void goToMap(View view)
    {
        Intent goToMap = new Intent(this,MapsActivity.class);
        startActivity(goToMap);
    }

}
