package com.guoyucao.memefier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.guoyucao.memefier.gifencoder.BitmapExtractor;
import com.guoyucao.memefier.gifencoder.GIFEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final String FILE_NAME = String.valueOf(System.currentTimeMillis()) + ".gif";
    int fps;
String PATH;
    Uri videoUri;
    double extractFrequency;
   int GIF_WIDTH;
    private Context mContext;
ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

    public void setListener(EncodeListener listener) {
        this.listener = listener;
    }

    EncodeListener listener;
    public FileManager(int fps, Uri uri, int GIF_WIDTH,Context context, String PATH) {
        this.fps = fps;
       this.PATH = PATH;
        this.GIF_WIDTH = GIF_WIDTH;
        bitmaps = new ArrayList<Bitmap>();
         double extractFrequency = 1000 * 1000 / fps;
         //extractFrequency = 1000 / fps;
        videoUri = uri;
        mContext = context;
    }

    public void extractFrames() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext,videoUri);
        long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        for (int i = 0; i < duration; i += extractFrequency) {
            Bitmap bitmap = retriever.getFrameAtTime((long)i, MediaMetadataRetriever.OPTION_CLOSEST);
         //   GIF_WIDTH = bitmap.getWidth();
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, GIF_WIDTH, bitmap.getHeight() * GIF_WIDTH / bitmap.getWidth(), false); // 根据GIF的大小Resize Bitmap
            if (bitmap != scaleBitmap && !bitmap.isRecycled()) {
                bitmap.recycle(); // 回收原始Bitmap
            }
            //addFrameToEncoder(scaleBitmap); // 将Bitmap图象传入GIFEncoder中进行编码
            bitmaps.add(scaleBitmap);
        }
    }






    public void encodeFrames(){

       BitmapExtractor extractor = new BitmapExtractor();
        extractor.setFPS(1);
        extractor.setScope(0, 3);
        extractor.setSize(480, 360);
        List<Bitmap> bs = extractor.createBitmaps(PATH,mContext,videoUri);

        try {
          String FILE_NAME = String.valueOf(System.currentTimeMillis()) + ".gif";
            File cacheFile = new File(mContext.getCacheDir(), FILE_NAME);
           String exportPath = mContext.getCacheDir() + "/" + FILE_NAME;


          //   String fileName = String.valueOf(System.currentTimeMillis()) + ".gif";
           //   String exportPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
        //    File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", fileName);

              Log.d("File Saved to: ",exportPath);
          GIFEncoder encoder = new GIFEncoder();
            encoder.init(bs.get(0));
            encoder.start(exportPath);
            for (int i = 1; i < bs.size(); i++) {
                encoder.addFrame(bs.get(i));
            }
            encoder.finish();
       //     galleryAddPic(cacheFile);
            Intent myIntent = new Intent(mContext, PreviewActivity.class);// messaging object
            myIntent.putExtra("path", exportPath );
            mContext.startActivity(myIntent);



            cacheFile.delete();

        }catch (Exception e){
            listener.error();

        }
        /*
     //   File outputFile = new File("temp.gif");
        File cacheFile = new File(mContext.getCacheDir(), "temp.gif");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fos != null) {
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.start(fos);

            for (Bitmap bitmap : bitmaps) {
                gifEncoder.addFrame(bitmap);
            }

            gifEncoder.finish();
            galleryAddPic(cacheFile);
            cacheFile.delete();
            listener.onSaveReady();
        }


         */
      //  listener.error();

    }


    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
       // File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
        listener.onSaveReady();
    }


    public interface EncodeListener {
        void onSaveReady();
        void error();
    }
}
