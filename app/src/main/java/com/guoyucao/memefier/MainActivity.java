package com.guoyucao.memefier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
   /*
  String[] resolution = { "320P", "480P", "960P", "1080P"};
   Spinner spin1;
   */

    ImageButton addVideoBtn;
    ImageButton convertVideoBtn;
    LinearLayout addVideoView;
    VideoView videoView;
    ProgressBar progressBar;
    TextView convertLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        spin1 = (Spinner) findViewById(R.id.resolution);
        Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
       aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Setting the ArrayAdapter data on the Spinner
       spin1.setAdapter(aa);
      */

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