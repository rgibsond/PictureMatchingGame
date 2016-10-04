package com.example.rgdrys13.picturematchinggame;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class GameFragment extends Fragment {

    public static final int NUMBER_OF_PAIRS = 12;

    Initializer initializer;
    Button resetButton, winButton;
    int clickCount;
    ArrayList<ImageButton> selected;
    TextView winText;
    SoundPool sp;
    int beam, buzz, airhorn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        clickCount = 0;
        final View root = inflater.inflate(R.layout.fragment_game, container);
        winText = (TextView) root.findViewById(R.id.win_text);
        initializer = new Initializer(this);
        resetButton = (Button) root.findViewById(R.id.my_button);
        winButton = (Button) root.findViewById(R.id.check_win_button);
        initializer.initViews(root);
        selected = new ArrayList();
        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        beam = sp.load(getContext(), R.raw.beamsound, 1);
        buzz = sp.load(getContext(), R.raw.buzzsound,1);
        airhorn = sp.load(getContext(), R.raw.airhorn,1);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializer.resetGame(root);
                clickCount = 0;
            }
        });

        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected.size()==NUMBER_OF_PAIRS*2)
                    winCondition();
            }
        });

        return root;
    }

    public void onImageClick(View view, Resources res)  {
        ImageButton image = (ImageButton)view;
        if (image.isSelected())
            return;
        if (!initializer.correct.contains(initializer.imageGridLocations[initializer.buttonLocations.get(image).x][initializer.buttonLocations.get(image).y])) {
            if (selected.size() < initializer.correct.size() * 2 + 2) {
                clickCount++;
                initializer.countText.setText("Click Count: " + clickCount);
                if (!initializer.correct.contains(initializer.buttonLocations.get(image))) {
                    if (view.isSelected()) {
                        new PictureLoaderTask(image, res).execute(R.drawable.slushield);
                        //image.setImageDrawable(res.getDrawable(R.drawable.slushield,null));
                        view.setSelected(false);
                        //selected.remove(image);
                    } else {
                        new PictureLoaderTask(image, res).execute(initializer.imageGridLocations[initializer.buttonLocations.get(image).x][initializer.buttonLocations.get(image).y]);
                        view.setSelected(true);
                        selected.add(image);
                    }
                }

            } else {


            //if (selected.size() == initializer.correct.size() * 2 + 2) {
                int samePicture = checkSame(selected.get(selected.size() - 1), selected.get(selected.size() - 2));
                if (samePicture != 0) {
                    initializer.correct.add(samePicture);
                    sp.play(beam, 1f, 1f, 1, 0, 1);
                }else{
                    sp.play(buzz, 0.1f, 0.1f, 1, 0, 1);
                }

                while (selected.size() > initializer.correct.size() * 2) {
                    ImageButton ib = selected.get(initializer.correct.size() * 2);
                    new PictureLoaderTask(ib, res).execute(R.drawable.slushield);
                    selected.remove(ib);
                    ib.setSelected(false);

                }
            }
        }
    }

    //if true, returns ID of image
    // if false, returns 0
    public int checkSame(ImageButton first, ImageButton second){
        int firstPictureID = initializer.imageGridLocations[initializer.buttonLocations.get(first).x][initializer.buttonLocations.get(first).y];
        int secondPictureID = initializer.imageGridLocations[initializer.buttonLocations.get(second).x][initializer.buttonLocations.get(second).y];
        return  (firstPictureID == secondPictureID ? firstPictureID:0);
    }

    public void winCondition(){
        sp.play(airhorn, 1f, 1f, 1, 3, 1f);
        winText.setVisibility(View.VISIBLE);
    }

}
