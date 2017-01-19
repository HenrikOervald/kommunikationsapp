package com.example.oervald.kommunikationsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Henrik on 27-04-2015.
 */
public class SubMenuImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> itemList = new ArrayList<String>();
    String classTitle;

    public SubMenuImageAdapter(Context c, String classTitle) {
        mContext = c;
        this.classTitle = classTitle;
        loadPicturePaths();
    }

    private void add(String path) {
        itemList.add(path);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);


        } else {
            imageView = (ImageView) convertView;
        }
        if (!itemList.get(position).isEmpty()) {
            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);

            imageView.setImageBitmap(bm);
        }
        return imageView;

    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public void loadPicturePaths() {

        File file = new File(mContext.getFilesDir(), classTitle);
        System.out.println(file.getAbsolutePath());

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String text;

            while ((line = br.readLine()) != null) {
                text = line;
                String [] bits = text.split(",");
                CostumObject cust = new CostumObject(bits[0], bits[1]);
                add(cust.imgPath);

            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            Toast.makeText(mContext, "Der er tomt", Toast.LENGTH_SHORT).show();
        }


    }


}





