package com.fourdea.a360viewerdemoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dhrumil on 8/11/2016.
 */
public class MyImageDownloader {

    String TAG = "MyDownloader";

    Context context;

    public MyImageDownloader(Context context){
        this.context = context;
    }


    protected Bitmap downloadImageWithCaching(final Context ctx, String uri,String shortUrl, int sceneNum, String quality) throws IOException
    {

        //get file name
        String tempFilename = uri.substring(uri.lastIndexOf('/') + 1);
        String filename = tempFilename.substring(0,1).toUpperCase() + tempFilename.substring(1);

//        Log.d(TAG, "Checking if file is cached: " + shortUrl+" "+sceneNum+" " +filename);

        //check if file is cached
        if(!doesCacheFileExist(sceneNum, filename, shortUrl, quality)){
//            Log.d(TAG, "No cache exists. Downloading.");
            URL url = new URL(uri);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            Log.d(TAG, "connection open.");

            /*get inputStream
            * store byte by byte into byteBuffer
            * and then make bitmap from byteBuffer
            * */
            byte[] data = new byte[1024];
            byte[] bitmapData = new byte[1024];
            InputStream is = con.getInputStream();
//            int bytesRead = 0, bytesTotal=con.getContentLength();
//            long bytesDownloaded = 0;
//            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            Bitmap b = null;
//            while((bytesRead = is.read(data)) != -1){
//                bytesDownloaded += bytesRead;
//                byteBuffer.write(data, 0, bytesRead);
//                highDownloadListener.setProgress(bytesRead);
//                MyLog.i("Percentage","done: "+ (bytesDownloaded*100)/bytesTotal);
//            }
            //MainActivity.mainFrame.removeView(MainActivity.v);

//            bitmapData = byteBuffer.toByteArray();

            //Bitmap b = BitmapFactory.decodeStream(data);
//            Log.d(TAG, "file size:" +  con.getContentLength());
//            Log.d(TAG, "Byte array data: " +  data);
//            Log.d(TAG, "Byte array bitmapData: " +  bitmapData);
            b = BitmapFactory.decodeStream(is);

            is.close();
            con.disconnect();
            //b = postProcessBitmap(b);
//            b = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            //cache bitmap
            cacheBitmap(sceneNum, b, filename, shortUrl, quality);

            return b;
        }
        else{
//            Log.d(TAG, "Found cached file.");
            //get cached bitmap
            return getCachedBitmap(sceneNum, filename,quality, shortUrl);
        }
    }

    protected Boolean doesCacheFileExist(int sceneNum, String filename,String shortUrl, String quality)
    {
        String basePath = Environment.getExternalStorageDirectory()+"/4Dea vTour/.My Tours/" + shortUrl +"/";

        String sceneFolderPath = basePath + sceneNum +"/";

        String qualityFolderPath = sceneFolderPath + quality +"/";

        File fileDir = new File(basePath);
        if (!fileDir.exists()){
            fileDir.mkdir();
        }

        File sceneDir = new File(sceneFolderPath);
        if(!sceneDir.exists()){
            sceneDir.mkdir();
        }

        File qualityDir = new File(qualityFolderPath);
        if(!qualityDir.exists()){
            qualityDir.mkdir();
        }

//        File cacheDir = ctx.getCacheDir();

        filename = sceneNum+quality+filename;
        File cacheFile = new File(context.getCacheDir(), filename);

        return (cacheFile.exists() && cacheFile.getTotalSpace()>10000);
    }

    protected void cacheBitmap(int sceneNum, Bitmap b, String filename, String shortUrl, String quality) throws IOException
    {
        String basePath = Environment.getExternalStorageDirectory() + "/4Dea vTour/.My Tours/";
        File baseDir = new File(basePath);
        if(!baseDir.exists()){
            baseDir.mkdir();
        }

        String tourPath = basePath + "/" + shortUrl +"/";
        File tourDir = new File(tourPath);
        if(!tourDir.exists()){
            tourDir.mkdir();
        }

        String scenePath = tourPath + "/" + sceneNum +"/";
        File sceneDir = new File(scenePath);
        if(!sceneDir.exists()){
            sceneDir.mkdir();
        }

        String qualityPath = scenePath + "/" + quality +"/";
        File qualityDir = new File(qualityPath);
        if(!qualityDir.exists()){
            qualityDir.mkdir();
        }

//        File cacheDir = ctx.getCacheDir();

//        File nomedia = new File(qualityDir, ".nomedia");
//        try {
//            nomedia.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        filename = shortUrl+sceneNum+quality+filename;
        File cacheFile = new File(context.getCacheDir(), filename);

        FileOutputStream fos = new FileOutputStream(cacheFile);

        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close();
        fos.flush();
    }

    protected Bitmap getCachedBitmap(int sceneNum, String filename, String quality, String shortUrl) throws IOException
    {
        String basePath = Environment.getExternalStorageDirectory()+"/4Dea vTour/.My Tours/"+shortUrl+"/";

        String filePath = basePath+ sceneNum +"/"+quality+ "/" +filename;
        filePath = context.getCacheDir().getAbsolutePath()+"/"+ shortUrl+sceneNum+quality+filename;

        return getBitmapFromPath(2f, filePath);
    }


    public Bitmap getBitmapFromPath(float MP, String path) throws IOException {
        final int IMAGE_MAX_SIZE = (int) (MP*1000000); // 1MP

        File file = new File(path);

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, o);

        int scale = 1;
        if(o.outWidth > 2000){
//                scale = 2;
        }
        else {
            scale = 1;
        }
        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                IMAGE_MAX_SIZE) {
            scale++;
        }
//        Log.d(TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

        Bitmap b = null;
        if (scale > 1) {
            //scale--;
            // scale to max possible inSampleSize that still yields an image
            // larger than target
            o = new BitmapFactory.Options();
            o.inSampleSize = scale;
//            b = BitmapFactory.decodeFile(path, o);
            FileInputStream fileInputStream = new FileInputStream(file);
            FlushedInputStream flushedInputStream = new FlushedInputStream(fileInputStream);
            b = BitmapFactory.decodeStream(new FlushedInputStream(fileInputStream));
            flushedInputStream.close();
            fileInputStream.close();

            // resize to desired dimensions
            int height = b.getHeight();
            int width = b.getWidth();
//            Log.d(TAG, "1st scale operation dimenions - width: " + width + ", height: " + height);

            double y = Math.sqrt(IMAGE_MAX_SIZE
                    / (((double) width) / height));
            double x = (y / height) * width;

            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
//                        (int) y, true);
//                b.recycle();
            //b = scaledBitmap;

            System.gc();
        } else {
            FileInputStream fileInputStream = new FileInputStream(file);
            FlushedInputStream flushedInputStream = new FlushedInputStream(fileInputStream);
            b = BitmapFactory.decodeStream(new FlushedInputStream(fileInputStream));
            flushedInputStream.close();
            fileInputStream.close();
        }

//        Log.d(TAG, "decoded stream bitmap: "+path+" " +b);
//        Log.d(TAG, "bitmap size - width: " + b.getWidth() + ", height: " +
//                b.getHeight());
        return b;
    }

    class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
