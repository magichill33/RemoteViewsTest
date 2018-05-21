package com.morefun.test;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "Main2Activity";
    Button btnAdd,btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnStart = (Button) findViewById(R.id.btn_startService);
        btnStart.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*Log.i(TAG,"onServiceConnected");
            IViewManager viewsManager = IViewManager.Stub.asInterface(service);
            try {
                viewsManager.setTextViewText(R.id.tv_text,"通过AIDL跨进程修改TextView内容");
                viewsManager.addView(R.layout.layout);
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
            IViewManager remoteViewsManager = IViewManager.Stub.asInterface(service);
            RemoteViews remoteViews = new RemoteViews(Main2Activity.this.getPackageName(),R.layout.layout);

            /*Intent intentClick = new Intent(Main2Activity.this,MainActivity.class);
            PendingIntent openMainActivity = PendingIntent.getActivity(Main2Activity.this,0,intentClick,0);
            remoteViews.setOnClickPendingIntent(R.id.firstButton,openMainActivity);

            Intent secondClick = new Intent(TempActivity.this,LoginActivity.class);
            PendingIntent openLoginActivity = PendingIntent.getActivity(Main2Activity.this,0,secondClick,0);
            remoteViews.setOnClickPendingIntent(R.id.secondButton,openLoginActivity);*/
            try {
                remoteViewsManager.addRemoteView(remoteViews);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"onServiceDisconnected");
        }
    };

    private void bindService(){
        Intent viewServiceIntent = new Intent(this,ViewAIDLService.class);
        bindService(viewServiceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startService:
                bindService();
                break;
            case R.id.btn_add:
                break;
        }
    }
}
