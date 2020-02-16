package com.example.aashish.portal;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {
    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    @TargetApi(Build.VERSION_CODES.O)
    private Context mCtx;


    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showBigNotification(String title, String message, String url, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.u).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.u)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.u))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager1 = (NotificationManager)mCtx.getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            CharSequence name = "Application";
            String description = "Notifications";
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            notificationManager1.createNotificationChannel(mChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mCtx.getApplicationContext(),"id_product")
                    .setSmallIcon(R.mipmap.u)
                    .setChannelId(id)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.u))
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis());
            notificationManager1.notify(1, notificationBuilder.build());
        }
    }

    public void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.u).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.u)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.u))
                .setContentText(message)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager1 = (NotificationManager)mCtx.getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            CharSequence name = "Application";
            String description = "Notifications";
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            notificationManager1.createNotificationChannel(mChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mCtx.getApplicationContext(),"id_product")
                    .setSmallIcon(R.mipmap.u)
                    .setChannelId(id)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis());
            notificationManager1.notify(1, notificationBuilder.build());
        }
        }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}