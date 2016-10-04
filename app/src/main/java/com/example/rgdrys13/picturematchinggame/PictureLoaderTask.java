package com.example.rgdrys13.picturematchinggame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageButton;

/**
 * Created by rgdrys13 on 9/30/2016.
 */

public class PictureLoaderTask extends AsyncTask<Integer, Void, Bitmap> {

    private ImageButton ib;
    private int w,h;
    private Resources res;

    public PictureLoaderTask(ImageButton v, Resources res){
        ib = v;
        this.res = res;
        this.w = v.getWidth();
        this.h = v.getHeight();
    }

    @Override
    protected Bitmap doInBackground(Integer... id) {
        return Utility.decodeSampledBitmapFromResource(res,id[0], w, h);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ib.setImageBitmap(bitmap);
    }
}
