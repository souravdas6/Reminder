package myapp.sample.com.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private String mDate;
    private String mTime;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Toast.makeText(context, "I am here",
                    Toast.LENGTH_SHORT).show();

            ReminderDatabase rb = new ReminderDatabase(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<Reminder> reminders = rb.getAllReminders();

            for (Reminder rm : reminders) {
                mReceivedID = rm.getID();
                mDate = rm.getDate();
                mTime = rm.getTime();

                mDateSplit = mDate.split("/");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, mMonth - 1);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);


                // Create a new notification
                mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID);
            }
        }
    }
}