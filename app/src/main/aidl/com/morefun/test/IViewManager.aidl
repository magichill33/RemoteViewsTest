// IViewManager.aidl
package com.morefun.test;

// Declare any non-default types here with import statements

interface IViewManager {
   void setTextViewText(in int id,in String text);//设置TextView的内容
   void addView(in int layoutId);                 //添加View视图
   void addRemoteView(in RemoteViews remoteViews);
}
