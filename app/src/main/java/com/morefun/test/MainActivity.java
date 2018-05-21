package com.morefun.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private final static String TAG = "MainActivity";
    private static LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();
        mLinearLayout = (LinearLayout) findViewById(R.id.parentPanel);
        Button button = (Button) findViewById(R.id.btn_goto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotoActivity();
            }
        });
    }

    private void gotoActivity(){
        /*Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);*/
    }

    public void showNotification() {
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = "天意博文";
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ceshi",0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.tv,"天意博文textview");
        remoteViews.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        remoteViews.setTextColor(R.id.tv,getResources().getColor(R.color.colorPrimaryDark));
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, new Intent(this, Main2Activity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv,pendingIntent1);
        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }

    public static class MyHandler extends Handler {
        WeakReference<Context> weakReference;
        public MyHandler(Context context, Looper looper) {
            super(looper);
            weakReference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage");
            switch (msg.what) {
                case 1: //RemoteViews的AIDL实现
                    RemoteViews remoteViews = msg.getData().getParcelable("remoteViews");
                    if (remoteViews != null) {
                        Log.i(TAG, "updateUI");

                        View view = remoteViews.apply(weakReference.get(), mLinearLayout);
                        mLinearLayout.addView(view);
                    }
                    break;
                case 2: //修改MainActivity中TextView的内容
                    Bundle bundle = msg.getData();
                    TextView textView = (TextView) mLinearLayout.findViewById(bundle.getInt("id"));
                    textView.setText(bundle.getString("text"));
                    break;
                case 3: //在MainActivity中添加View视图
                    LayoutInflater inflater = LayoutInflater.from(weakReference.get());
                    View view = inflater.inflate(msg.getData().getInt("layoutId"),null);
                    mLinearLayout.addView(view);
                default:
                    break;
            }
        }

    };
}
