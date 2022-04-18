package com.guoyucao.memefier;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.guoyucao.memefier.databinding.ActivityPreviewBinding;

import java.io.File;

import pl.droidsonroids.gif.GifAnimationMetaData;
import pl.droidsonroids.gif.GifImageView;

public class PreviewActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPreviewBinding binding;
    GifImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);



        String path = getIntent().getExtras().getString("path");
        File imgFile = new File("path");
        preview = findViewById(R.id.preview);
        preview.setImageURI(Uri.fromFile(imgFile));


    }


}