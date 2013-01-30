package com.robgthai.example.DropViewNotificationSample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dropview.DropViewActivity;
import com.dropview.DropViewController;

/**
 * Creator: Poohdish Rattanavijai
 * Date: 1/30/13
 * Time: 3:21 PM
 * Version: 1.00
 */
public class DefaultSampleActivity extends DropViewActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_sample);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.github_view, null, false);

        v.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String url = "https://github.com/zodio/DropViewNotification";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        setTopView(v);
    }

    public void show(View v){
        getDropViewController().show(DropViewController.Position.TOP);
    }

    public void dismiss(View v){
        getDropViewController().dismiss(DropViewController.Position.TOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.default_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_switch: switchActivity();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(){
        Intent i = new Intent(this, CustomAnimationSampleActivity.class);
        startActivity(i);
        this.finish();
    }
}