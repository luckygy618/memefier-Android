package com.guoyucao.memefier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    ImageButton addVideoBtn;
    ImageButton convertVideoBtn;
    LinearLayout addVideoView;
    VideoView videoView;
    ProgressBar progressBar;
    TextView convertLabel;
NumberPicker resolution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addVideoBtn = findViewById(R.id.addVideo);
        convertVideoBtn = findViewById(R.id.convertVideo);
        addVideoView = findViewById(R.id.addVideoView);
        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        convertLabel = findViewById(R.id.convertLabel);
        convertVideoBtn.setOnClickListener(convertListener);
        addVideoBtn.setOnClickListener(addVideoListener);

        resolution = (NumberPicker)findViewById(R.id.resolution);
        resolution.setMinValue(0);
       resolution.setMaxValue(((MyApp)getApplication()).getResolution_option().length-1);
      resolution.setDisplayedValues(((MyApp)getApplication()).getResolution_option());
        //resolution.setWrapSelectorWheel(false);
        resolution.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

    }


    public void reset() {

        videoView.setVisibility(View.GONE);
        addVideoBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        convertLabel.setText(R.string.beforeConvert);
        convertVideoBtn.setVisibility(View.VISIBLE);
    }

    protected View.OnClickListener addVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addVideo(v);
        }
    };

    public void addVideo(View v) {

        videoView.setVisibility(View.VISIBLE);
        addVideoBtn.setVisibility(View.GONE);

    }

    protected View.OnClickListener convertListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            convertBtnClicked(v);
        }
    };

    public void convertBtnClicked(View v) {

        videoView.setVisibility(View.VISIBLE);
        addVideoBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        convertLabel.setText(R.string.converting);
        convertVideoBtn.setVisibility(View.GONE);
    }


}