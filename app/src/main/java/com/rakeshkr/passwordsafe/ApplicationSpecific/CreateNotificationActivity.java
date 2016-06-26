package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;

public class CreateNotificationActivity {

    Context notificationContext;
    SharedPreferences sharedPreferences;
    int notifyId;
    int numMsg;
        public CreateNotificationActivity(Context context){
            notificationContext=context;
            notifyId=9999;
            numMsg=0;
            sharedPreferences=context.getSharedPreferences(MySharedPreferences.NOTIFICATIONPREFERENCES,Context.MODE_PRIVATE);
        }

    void newNonClickableNotification(String title,String msg){

        int numMsg=0;
        NotificationManager mNotificationManager =
                (NotificationManager) notificationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(notificationContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setNumber(++numMsg)
                        .setLights(Color.GREEN,3000,3000);
        if (sharedPreferences.getBoolean(MySharedPreferences.Vibration,false)){
            mBuilder.setVibrate(new long[] { 1000,1000 });
        }
        if (sharedPreferences.getBoolean(MySharedPreferences.Sound,false)) {
            mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + notificationContext.getPackageName() + "/raw/notification"));
        }
        NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
        String[] events = {msg};
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Detailed Information");

        // Moves events into the expanded layout
        for (String s:events) {
            inboxStyle.addLine(s);
        }
        mBuilder.setStyle(inboxStyle);
        // notifyId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    void newClickableNotification(String title,String msg){


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(notificationContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setNumber(++numMsg)
                        .setLights(Color.GREEN, 3000, 3000);

        if (sharedPreferences.getBoolean(MySharedPreferences.Vibration,false)){
            mBuilder.setVibrate(new long[] { 1000,1000});
        }
        if (sharedPreferences.getBoolean(MySharedPreferences.Sound,false)){

            mBuilder.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+notificationContext.getPackageName()+"/raw/notification"));
        }
        NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
        String[] events = {msg};
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Detailed Information");

        // Moves events into the expanded layout
        for (String s:events) {
            inboxStyle.addLine(s);
        }

        mBuilder.setStyle(inboxStyle);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(notificationContext, LockActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
       PendingIntent pendingIntent=PendingIntent.getActivity(notificationContext,0,resultIntent,0);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) notificationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // notifyId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }
}
