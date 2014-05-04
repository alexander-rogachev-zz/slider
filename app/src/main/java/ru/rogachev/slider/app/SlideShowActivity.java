package ru.rogachev.slider.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FilenameFilter;

import ru.rogachev.slider.utils.Constants;


public class SlideShowActivity extends Activity implements Animation.AnimationListener {

    private ImageView ivSlide;
    private Animation animFadeIn;
    private Animation animFadeOut;
    private Animation animTransLeft;
    private Animation animTransRight;

    RefreshHandler refreshHandler = new RefreshHandler();

    int i = 0;

    private File[] images;

    private int delay = 1;

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            SlideShowActivity.this.updateUI();
        }
        public void sleep(long delayMillis){
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    public void updateUI() {
        refreshHandler.sleep(delay);
        if (i + 1 < images.length) {
            i++;
        } else {
            i = 0;
        }
        Uri uri = Uri.fromFile(images[i]);
        ivSlide.setImageURI(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        SharedPreferences sharedPref = getSharedPreferences(Constants.APP_PREFERENCES_NAME, MODE_PRIVATE);
        String folderName = sharedPref.getString(Constants.FOLDER_PARAM_NAME, null);
        String delayPref = sharedPref.getString(Constants.DELAY_PARAM_NAME, null);
        String[] timeItems = delayPref.split(":");
        int hours = Integer.parseInt(timeItems[0]);
        int minutes = Integer.parseInt(timeItems[1]);
        int seconds = Integer.parseInt(timeItems[2]);
        delay = (hours * 3600 + minutes * 60 + seconds) * 1000;
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        File folder = new File(folderName);
        registerReceiver(closeSliderReceiver, new IntentFilter(Constants.CLOSE_SLIDER_INTENT_NAME));
        ivSlide = (ImageView) findViewById(R.id.imageView);
        if (folder.isDirectory()) {
            images = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String fileName) {
                    File tempFile = new File(String.format("%s/%s", file.getPath(), fileName));
                    return !tempFile.isFile() || tempFile.getName().matches(".*\\.jpg");
                }
            });
            updateUI();
        }

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        animFadeIn.setAnimationListener(this);
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);
        animFadeOut.setAnimationListener(this);
        animTransLeft = AnimationUtils.loadAnimation(this, R.anim.anim_trans_left);
        animTransLeft.setAnimationListener(this);
        animTransRight = AnimationUtils.loadAnimation(this, R.anim.anim_trans_right);
        animTransRight.setAnimationListener(this);
        ivSlide.startAnimation(animFadeIn);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadeIn) {
            ivSlide.startAnimation(animFadeOut);
        } else if (animation == animFadeOut){
            ivSlide.startAnimation(animTransRight);
        } else if (animation == animTransRight) {
            ivSlide.startAnimation(animTransLeft);
        } else if (animation == animTransLeft) {
            ivSlide.startAnimation(animFadeIn);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private final BroadcastReceiver closeSliderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeSliderReceiver);
    }

}
