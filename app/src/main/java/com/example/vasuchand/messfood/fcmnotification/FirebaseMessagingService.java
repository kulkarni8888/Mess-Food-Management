package com.example.vasuchand.messfood.fcmnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.vasuchand.messfood.MainActivity;
import com.example.vasuchand.messfood.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

/**
 * Created by Vasu Chand on 4/4/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage message) {

        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

       // notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifysnd);
       // notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
       // System.out.println("**************" + message.getData() + "  " +message.getMessageId() + "m" +message.getData());

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(message.getData().get("body"))
                .setContentText(message.getData().get("title"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message.getData().get("title")))
                .setSmallIcon(R.drawable.notification)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(m,builder.build());
    }

}
