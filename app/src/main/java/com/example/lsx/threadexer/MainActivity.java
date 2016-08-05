package com.example.lsx.threadexer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImageView;
    private Button mLoadImageButton;
    private Button mShowToastButton;
    private ProgressBar mProgressBar;
    private  Handler mHandler= new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mProgressBar.setProgress((int)msg.obj);
                    break;
                case 2:
                    mImageView.setImageBitmap((Bitmap) msg.obj);
                    mProgressBar.setVisibility(View.INVISIBLE);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.activity_main_image_view);
        mLoadImageButton = (Button) findViewById(R.id.activity_main_load_image_button);
        mShowToastButton = (Button) findViewById(R.id.activity_main_show_toast_button);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                //Message不用每次都进行new，可以从已有的获取
                                Message msg = mHandler.obtainMessage();
                                //表示让progressbar显示出来
                                msg.what = 0;
                                mHandler.sendMessage(msg);
                                for (int i =1; i<11;i++){
                                    sleep();
                                    Message msg2 = mHandler.obtainMessage();
                                    //表示让progressbar往前走
                                    msg2.what = 1;
                                    msg2.obj = i*10;
                                    mHandler.sendMessage(msg2);
                                }
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.ic_launcher);
                                Message msgBitmap = mHandler.obtainMessage();
                                msgBitmap.what = 2;
                                msgBitmap.obj = bitmap;
                                mHandler.sendMessage(msgBitmap);


                            }

                            private void sleep() {
                                try{
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();
            }
        });
        mShowToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "xxxxxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    class LoadImageTask extends AsyncTask<Void,Integer,Bitmap>{
//        @Override
//        protected void onPreExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... params) {
//            for (int i =1; i<11; i++){
//                sleep();
//                publishProgress(i*10);
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//            Log.d(TAG, "线程doInBackground: "+Thread.currentThread().getName());
//            return bitmap;
//        }
//
//        private void sleep() {
//            try{
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            mImageView.setImageBitmap(bitmap);
//            mProgressBar.setVisibility(View.INVISIBLE);
//            Log.d(TAG, "线程onPostExecute: "+Thread.currentThread().getName());
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            mProgressBar.setProgress(values[0]);
//        }
//    }


}
