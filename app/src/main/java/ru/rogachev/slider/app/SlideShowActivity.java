package ru.rogachev.slider.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FilenameFilter;

import ru.rogachev.slider.utils.Constants;


public class SlideShowActivity extends Activity {

    private ImageView ivSlide;

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
        SharedPreferences sharedPref = getSharedPreferences("ru.rogachev.slider.app_preferences", MODE_PRIVATE);
        String folderName = sharedPref.getString(Constants.FOLDER_PARAM_NAME, "");
        String delayPref = sharedPref.getString(Constants.DELAY_PARAM_NAME, "");
        String[] timeItems = delayPref.split(":");
        int hours = Integer.parseInt(timeItems[0]);
        int minutes = Integer.parseInt(timeItems[1]);
        int seconds = Integer.parseInt(timeItems[2]);
        delay = (hours * 3600 + minutes * 60 + seconds) * 1000;

        File folder = new File(folderName);

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
    }

}
