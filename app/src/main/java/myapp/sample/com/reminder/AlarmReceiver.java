package myapp.sample.com.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    final static String GROUP_KEY = "reminder_key";

    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    Notification notification;
    NotificationManagerCompat manager;
    String content = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        int mReceivedID = Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_REMINDER_ID));

        // Get notification title from Reminder Database
        ReminderDatabase rb = new ReminderDatabase(context);
        Reminder reminder = rb.getReminder(mReceivedID);
        String mTitle = reminder.getTitle();


        // Create Notification
        manager = NotificationManagerCompat.from(context);
        manager.cancelAll();
        createNotification(context, mReceivedID, mTitle);

    }

    private void createNotification(Context context, int mReceivedID, String mTitle) {
        // Create intent to open ReminderEditActivity on notification click
        Intent editIntent = new Intent(context, MainActivity.class);
        editIntent.putExtra(MainActivity.EXTRA_REMINDER_ID, Integer.toString(mReceivedID));
        PendingIntent mClick = PendingIntent.getActivity(context, mReceivedID, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        notification = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setTicker(mTitle)
                .setContentText(mTitle)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setGroupSummary(true)
                .setGroup(GROUP_KEY)
                .setContentIntent(mClick)
                .build();
//      NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(mReceivedID, notification);
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(MainActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time

        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;
        Log.d(">target>", calendar.getTimeInMillis() + "");
        Log.d(">current>", currentTime + "");
        Log.d(">diff>", diffTime + "");

        // Start alarm using notification time
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}