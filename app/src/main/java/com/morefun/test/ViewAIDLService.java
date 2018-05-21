package com.morefun.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

public class ViewAIDLService extends Service {
    private static final String TAG = "ViewAIDLService";
    private Binder viewManager = new IViewManager.Stub(){
        @Override
        public void setTextViewText(int id, String text) throws RemoteException {
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putString("text",text);
            message.setData(bundle);
            new MainActivity.MyHandler(ViewAIDLService.this,getMainLooper()).sendMessage(message);
        }

        @Override
        public void addView(int layoutId) throws RemoteException {
            Message message = new Message();
            message.what = 3;
            Bundle bundle = new Bundle();
            bundle.putInt("layoutId",layoutId);
            message.setData(bundle);
            Log.i(TAG,"thread name = "+Thread.currentThread().getName());
            new MainActivity.MyHandler(ViewAIDLService.this,getMainLooper()).sendMessage(message);
        }

        @Override
        public void addRemoteView(RemoteViews remoteViews) throws RemoteException {
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putParcelable("remoteViews",remoteViews);
            message.setData(bundle);
            new MainActivity.MyHandler(ViewAIDLService.this,getMainLooper()).sendMessage(message);
        }

    };
    public ViewAIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return viewManager;
    }
}
