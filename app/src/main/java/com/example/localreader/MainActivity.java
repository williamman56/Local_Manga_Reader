package com.example.localreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        File storage = new File(Environment.getExternalStorageDirectory() + "/Manga");
        if (storage.isDirectory()) {
            ((TextView)(findViewById(R.id.header))).setText("Available Manga:");

            File[] manga_list = storage.listFiles();
            LinearLayout holder = (LinearLayout)findViewById(R.id.manga_holder);
            //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //holder.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (File manga : manga_list) {
                if (manga.isDirectory()) {
                    ConstraintLayout cl = new ConstraintLayout(this);
                    ImageView image = new ImageView(this);
                    TextView title = new TextView(this);
                    Button button = new Button(this);

                    ViewGroup.LayoutParams match_parent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    cl.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    button.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 330));
                    title.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

                    cl.setId(ViewCompat.generateViewId());
                    image.setId(ViewCompat.generateViewId());
                    title.setId(ViewCompat.generateViewId());
                    button.setId(ViewCompat.generateViewId());

                    title.setText(manga.getName());
                    title.setGravity(Gravity.CENTER_HORIZONTAL);
                    title.setTextColor(getResources().getColor(R.color.white));
                    title.setTypeface(ResourcesCompat.getFont(this, R.font.crimereg));
                    title.setTextSize(20f);
                    File thumbnail = new File(manga.getAbsolutePath() + "/thumbnail.jpg");
                    Bitmap bitImage = null;
                    if (thumbnail.isFile()) {
                        bitImage = handleImage(BitmapFactory.decodeFile(thumbnail.getAbsolutePath()));
                    }
                    else {
                        bitImage = handleImage(BitmapFactory.decodeResource(getResources(), R.drawable.thumbnail));
                    }
                    image.setImageBitmap(bitImage);

                    button.setTag(manga.getName());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = (String)v.getTag();
                            browseChapters(name);
                        }
                    });
                    button.getBackground().setAlpha(64);
                    cl.addView(button);
                    cl.addView(image);
                    cl.addView(title);

                    holder.addView(cl);

                    createConstraints(cl, image, title, button);
                }
            }
        }
    }

    private void createConstraints(ConstraintLayout cl, ImageView image, TextView title, Button button){
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cs.connect(image.getId(), ConstraintSet.LEFT, cl.getId(), ConstraintSet.LEFT, 32);
        cs.connect(title.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, 0);
        cs.connect(title.getId(), ConstraintSet.BOTTOM, cl.getId(), ConstraintSet.BOTTOM, 0);
        cs.connect(title.getId(), ConstraintSet.RIGHT, cl.getId(), ConstraintSet.RIGHT, 16);
        cs.connect(title.getId(), ConstraintSet.LEFT, image.getId(), ConstraintSet.RIGHT, 0);
        cs.connect(button.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, 0);
        cs.connect(button.getId(), ConstraintSet.BOTTOM, cl.getId(), ConstraintSet.BOTTOM, 0);
        cs.connect(image.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, 0);
        cs.connect(image.getId(), ConstraintSet.BOTTOM, cl.getId(), ConstraintSet.BOTTOM, 0);
        cs.applyTo(cl);
    }

    private Bitmap handleImage(Bitmap source) {
        int height = source.getHeight();
        int width = source.getWidth();
        int diff = Math.abs(height - width);
        Bitmap square = null;
        if (height != width) {
            if (height > width) {
                square = Bitmap.createBitmap(source, 0, (int)(diff / 2), width, height-diff);
            }
            else if (width > height) {
                square = Bitmap.createBitmap(source, (int)(diff / 2), 0, width-diff, height);
            }
        }
        else {
            square = source;
        }

        square = Bitmap.createScaledBitmap(square, 250, 250, true);
        return square;
    }

    private void browseChapters(String name) {
        String pathname = Environment.getExternalStorageDirectory() + "/Manga/" + name;
        Intent intent = new Intent(this, ChapterBrowser.class);
        intent.putExtra("pathname", pathname);
        startActivity(intent);
    }
}
