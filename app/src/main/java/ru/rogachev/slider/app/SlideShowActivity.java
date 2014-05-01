package ru.rogachev.slider.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FilenameFilter;

import ru.rogachev.slider.utils.Constants;


public class SlideShowActivity extends Activity {

    private ImageView ivSlide;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        sharedPref = getSharedPreferences("slider.settings", MODE_PRIVATE);
        String folderName = sharedPref.getString(Constants.FOLDER_PARAM_NAME, "");
        File folder = new File(folderName);
        ivSlide = (ImageView) findViewById(R.id.imageView);
        if (folder.isDirectory()) {
            File[] images = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String fileName) {
                    File tempFile = new File(String.format("%s/%s", file.getPath(), fileName));
                    if (tempFile.isFile())
                        return tempFile.getName().matches(".*\\.jpg");
                    return true;
                }
            });
            Uri uri = Uri.fromFile(images[0]);
            ivSlide.setImageURI(uri);
        }
    }

}
