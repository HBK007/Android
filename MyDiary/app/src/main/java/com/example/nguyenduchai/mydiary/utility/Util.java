package com.example.nguyenduchai.mydiary.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nguyen Duc Hai on 4/11/2017.
 */

public class Util {

    // save image to internal memory
   public static boolean saveImageToInternalMemory(Bitmap img, String fileName, Context context)
   {
       try {
           FileOutputStream fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE);
           img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
           fOut.flush();
           fOut.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return true;
   }

   // save image to storage data card
    public static boolean saveImageToSDCard(Bitmap image, String folder, String name)
    {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/";
        try{
            File dir = new File(fullPath);
            if(!dir.exists())
            {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath, name);
            if(!file.exists())
            {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCurrentDateTime()
    {
        // get datetime
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private static boolean isSDReadable()
    {
        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            mExternalStorageAvailable = true;
            Log.i("isSDReadable", "External storage card is readable");
        }else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            mExternalStorageAvailable = true;
            Log.i("isSDReadable", "External storage card is readable");
        }else{
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    public static Date convertStringToDate(String datetime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        try{
            Date date = simpleDateFormat.parse(datetime);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setBitmapToImage(final Context context, final String folder, final String name, final ImageView imageView)
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap)msg.obj;
                if(bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }
        };
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Util.readImage(folder, name, context);
                    Message message = new Message();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            });
            thread.start();
        }catch (Exception e)
        {

        }
    }

    public  static Bitmap readImage(String folder, String fileName, Context context)
    {
        Bitmap img = null;
        // read image from SD card
        String fullPatch = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/" + fileName;
        try{
            img = BitmapFactory.decodeFile(fullPatch);
        }catch (Exception e)
        {
            Log.i("DemoReadWriteImage", "Can not read image from SD card");
        }
        // read image from internal memory
        try{
            File file = context.getFileStreamPath(fileName);
            FileInputStream fIn = new FileInputStream(file);
            img = BitmapFactory.decodeStream(fIn);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            Log.i("DemoReadWriteImage", "Can not read image from internal memory");
        }
        return img;
    }

    public static String convertStringDateTimeToFileName(String date)
    {
        return date.toString().replace(":","").replace(" ","").replace("-","");
    }
}
