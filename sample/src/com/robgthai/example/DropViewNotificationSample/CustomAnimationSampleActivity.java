package com.robgthai.example.DropViewNotificationSample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dropview.DropViewActivity;
import com.dropview.DropViewController;

/**
 * Sample implementation of DropViewNotification
 */
public class CustomAnimationSampleActivity extends DropViewActivity implements DropViewController.OnDropViewAction {
    private static final String TAG = CustomAnimationSampleActivity.class.getSimpleName();
    TextView tv;
    ProgressBar progressBar;
    DropViewController.DropViewConfigurator dropViewConfigurator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getDropViewController().setTopMediaAction(this);
        setTextView();
    }

    private void setTextView(){
        progressBar = null;
        tv = new TextView(this);
        tv.setText("HELLO NOTIFICATION");
        tv.setBackgroundColor(Color.TRANSPARENT);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
//        tv.setTag("Notification textview");
        setTopView(tv);
    }

    private void setProgressBar(){
        tv = null;
        progressBar = new ProgressBar(this);
        progressBar.setHorizontalScrollBarEnabled(true);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        setTopView(progressBar);
    }

    private void setAnimation(int size, View mediaView, View contentView){
//        Log.d(TAG, "set custom animation from Activity: " + mediaView.getId());
//        Log.d(TAG, "mediaView.getTag(): " + mediaView.getTag());
//        Log.d(TAG, "parent.getTag(): " + ((View) mediaView.getParent()).getTag());
//        Log.d(TAG, "mediaView.isShown(): " + mediaView.isShown());

//        mediaView.setBackgroundColor(Color.GRAY);
        AnimatorSet aset = new AnimatorSet();
        ObjectAnimator aTop_translate = ObjectAnimator.ofFloat(mediaView, "translationY", -size, 0);
        ObjectAnimator aTop_alpha = ObjectAnimator.ofFloat(mediaView, "alpha", 1);
        aTop_translate.setInterpolator(new AccelerateDecelerateInterpolator());
        aTop_alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        // Push Content down
        AnimatorSet aset2 = new AnimatorSet();
        ObjectAnimator a2 = ObjectAnimator.ofFloat(contentView, "alpha", 0.7f);
        ObjectAnimator a2_1 = ObjectAnimator.ofFloat(contentView, "translationY", size);

        aset.playTogether(
                aTop_translate, aTop_alpha
        );

        aset2.playTogether(
                a2, a2_1
        );

        aTop_translate.setDuration(500);

        ObjectAnimator close_media = ObjectAnimator.ofFloat(mediaView, "alpha", 0);
        close_media.setDuration(500);
        AnimatorSet aset3 = new AnimatorSet();
        ObjectAnimator close_content = ObjectAnimator.ofFloat(contentView, "translationY", 0);
        ObjectAnimator close_content_1 = ObjectAnimator.ofFloat(contentView, "alpha", 1);

        aset3.playTogether(
                close_content, close_content_1
        );

        dropViewConfigurator = new DropViewController.DropViewConfigurator();
        dropViewConfigurator.setOpeningDVAnimator(aset)
                .setOpeningContentAnimator(aset2)
                .setClosingDVAnimator(close_media)
                .setClosingContentAnimator(aset3)
                .setOrder(DropViewController.DropViewConfigurator.Order.SEQUENTIAL_DROPVIEW_FIRST)
                .setAllDuration(500);

        getDropViewController().setTopDropViewAnimationConfig(dropViewConfigurator);
    }
    public void switchView(View v){
        if(tv != null){
            setProgressBar();
        }else{
            setTextView();
        }
    }

    public void openNotif(View v){
        getDropViewController().show(DropViewController.Position.TOP);
    }

    public void openSync(View v){
        dropViewConfigurator.setOrder(DropViewController.DropViewConfigurator.Order.SYNCHRONOUS);
        openNotif(v);
    }

    public void openMediaFirst(View v){
        dropViewConfigurator.setOrder(DropViewController.DropViewConfigurator.Order.SEQUENTIAL_DROPVIEW_FIRST);
        openNotif(v);
    }

    public void openMediaLast(View v){
        dropViewConfigurator.setOrder(DropViewController.DropViewConfigurator.Order.SEQUENTIAL_DROPVIEW_LAST);
        openNotif(v);
    }

    public void closeNotif(View v){
        getDropViewController().dismiss(DropViewController.Position.TOP);
    }

    @Override
    public View onPreViewAnimate(boolean isOpen) {
        return null;
    }

    @Override
    public View onPostViewAnimate(boolean isOpen) {
        return null;
    }

    @Override
    public void onViewAnimateException(DropViewController.Position position, boolean isOpen, View view, Exception exception) {

    }

    @Override
    public void onViewSizeMeasure(DropViewController.Position position, int size, View mediaView, View contentView) {
        Log.d(TAG, "onViewSizeMeasure: " + position + " - " + size);
        setAnimation(size, mediaView, contentView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.custom, menu);

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
        Intent i = new Intent(this, DefaultSampleActivity.class);
        startActivity(i);
        this.finish();
    }
}
