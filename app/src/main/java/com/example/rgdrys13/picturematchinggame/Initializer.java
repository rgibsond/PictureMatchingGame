package com.example.rgdrys13.picturematchinggame;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by rgdrys13 on 10/1/2016.
 */

public class Initializer {

    public static final int COLS = 4, ROWS = 6;
    private int[] imageIDs = {R.drawable.clee, R.drawable.edharcourt, R.drawable.ivan, R.drawable.jimdefranza,
            R.drawable.lisa, R.drawable.melville, R.drawable.patti_lock, R.drawable.rlock, R.drawable.komarov,
            R.drawable.rance, R.drawable.fox, R.drawable.mschuckers, R.drawable.meghan, R.drawable.labarge, R.drawable.musa, R.drawable.jeff};
    public int[][] imageGridLocations;
    private ArrayList<ImageButton> imageButtons;
    private Random random;
    public HashMap<ImageButton, Point> buttonLocations;
    private ArrayList<Point> possible;
    private GameFragment fragment;
    public TextView countText;
    public  ArrayList<Integer> correct;

    public Initializer(Fragment fragment){
        this.fragment = (GameFragment) fragment;
    }

    public void resetGame(View root){
        initViews(root);
        fragment.selected.clear();
        fragment.resetButton.setText("Restart Game");
    }

    public void initViews(View root){
        final Resources res = fragment.getResources();
        imageButtons = new ArrayList();
        fragment.winText.setVisibility(View.GONE);
        imageGridLocations = new int[COLS][ROWS];
        random = new Random();
        buttonLocations = new HashMap();
        correct = new ArrayList();
        countText = (TextView) root.findViewById(R.id.count_id);
        countText.setText("Click Count: 0");

        // get all of the blank image buttons
        for (int i = 0; i<COLS; i++){
            for (int j = 0; j<ROWS; j++){
                int id = res.getIdentifier("i"+i+"_"+j, "id", fragment.getActivity().getPackageName());
                ImageButton v = (ImageButton) root.findViewById(id);
                imageButtons.add(v);
                buttonLocations.put(v, new Point(i,j));
            }
        }

        for(ImageButton image: imageButtons){
            //new PictureLoaderTask(image, res).execute(R.drawable.slushield);
            image.setImageDrawable(res.getDrawable(R.drawable.slushield, null));
            image.setSelected(false);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.onImageClick(view, res);

                }
            });
        }

        possible = new ArrayList();
        for (int i = 0; i<COLS; i++){
            for (int j = 0; j<ROWS; j++){
                Point newPoint = new Point(i,j);
                possible.add(newPoint);
            }
        }
        // initialize image location
        ArrayList<Integer> list = new ArrayList();
        for (int id: imageIDs){
            list.add(id);
        }

        for (int i = 0; i<fragment.NUMBER_OF_PAIRS; i++){
            int id = list.get(random.nextInt(list.size()));
            list.remove((Integer)id);
            int count = 2;
            while (count>0){
                Point randomPoint = possible.get(random.nextInt(possible.size()));
                possible.remove(randomPoint);
                int x = randomPoint.x;
                int y = randomPoint.y;
                ImageButton selected = (ImageButton) root.findViewById(res.getIdentifier("i"+x+"_"+y, "id", fragment.getActivity().getPackageName()));
                if (!selected.isSelected()){
                    imageGridLocations[x][y] = id;
                    count--;
                }
            }
        }

    }

}
