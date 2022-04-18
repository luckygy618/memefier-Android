package com.guoyucao.memefier;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity implements FileManager.EncodeListener{

    private static final int REQUEST_SELECT_VIDEO = 1;
    private String filePath;
    AlertDialog dialog;

    private int STORAGE_PERMISSION_CODE = 1;


    ImageButton addVideoBtn;
    ImageButton convertVideoBtn;
    LinearLayout addVideoView;
    VideoView videoView;
    ProgressBar progressBar;
    TextView convertLabel;
NumberPicker resolution;
    TextView reset;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reset = findViewById(R.id.reset);
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
        reset.setOnClickListener(resetListener);
        resolution = (NumberPicker)findViewById(R.id.resolution);
        resolution.setMinValue(0);
       resolution.setMaxValue(((MyApp)getApplication()).getResolution_option().length-1);
      resolution.setDisplayedValues(((MyApp)getApplication()).getResolution_option());
       // resolution.setWrapSelectorWheel(false);
        resolution.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
         //   Toast.makeText(MainActivity.this, "You have already granted this permission!",
                //    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
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

    protected View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            videoView=null;
            videoView = findViewById(R.id.videoView);
            videoView.setVisibility(View.GONE);
            addVideoBtn.setVisibility(View.VISIBLE);
            filePath="";


        }
    };

    public void addVideo(View v) {

        videoView.setVisibility(View.VISIBLE);
        addVideoBtn.setVisibility(View.GONE);

        Intent intent = new Intent();
        intent.setType("video/*");
      //  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//allow select multiple videos
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_SELECT_VIDEO);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_VIDEO) {
            if (resultCode == RESULT_OK) {
                 videoUri = data.getData();
                filePath = getRealFilePath(videoUri);


                Log.d("Uri is: ", String.valueOf(videoUri));
                Log.d("File Path is: ", String.valueOf(filePath));
                videoView.setVideoPath(String.valueOf(videoUri));
                MediaController mediaController = new MediaController(this);
                //link mediaController to videoView
                mediaController.setAnchorView(videoView);
                //allow mediaController to control our videoView
                videoView.setMediaController(mediaController);
            //    videoView.start();
            }
        }
    }


    public String getRealFilePath(Uri uri ) {
        String path = uri.getPath();
        String[] pathArray = path.split(":");
        String fileName = pathArray[pathArray.length - 1];
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
    }


    protected View.OnClickListener convertListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            convertBtnClicked(v);
        }
    };

    public void convertBtnClicked(View v) {





        progressBar.setVisibility(View.VISIBLE);
        convertLabel.setText(R.string.converting);
        convertVideoBtn.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.convert);
        builder.setTitle(R.string.process);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

       FileManager f = new FileManager(1,videoUri,480,this,filePath);
        f.setListener(this);
        f.encodeFrames();

    }

    @Override
    public void onSaveReady() {
         Toast.makeText(this, "GIF Saved.", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        convertLabel.setText(R.string.beforeConvert);
    }

    @Override
    public void error(){
        Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        convertLabel.setText(R.string.beforeConvert);

    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

