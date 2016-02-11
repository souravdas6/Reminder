package myapp.sample.com.reminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView date, time;
    private Button setBtn;

    private ReminderDatabase rb;
    private AlarmReceiver alarmReceiver;
    private Calendar mCalendar;
    private Reminder reminder;

    private String[] mDateSplit;
    private String[] mTimeSplit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String mTime;
    private String mDate;

    public static String EXTRA_REMINDER_ID = "reminder_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting database
        rb = new ReminderDatabase(this);
        mCalendar = Calendar.getInstance();
        alarmReceiver = new AlarmReceiver();

        // initializing views
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);

        setBtn = (Button) findViewById(R.id.set_btn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = date.getText().toString().trim();
                mTime = time.getText().toString().trim();
                if (mDate.length() > 7 && mTime.length() == 5) {
                    setUpData();
                    saveReminder();
                } else {
                    Toast.makeText(getBaseContext(), "error in input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpData() {
        mDateSplit = mDate.split("/");
        mTimeSplit = mTime.split(":");

        mDay = Integer.parseInt(mDateSplit[0]);
        mMonth = Integer.parseInt(mDateSplit[1]);
        mYear = Integer.parseInt(mDateSplit[2]);
        mHour = Integer.parseInt(mTimeSplit[0]);
        mMinute = Integer.parseInt(mTimeSplit[1]);
    }

    public void saveReminder() {
        String mTitle = "Test" + Math.round((Math.random() * 100));
        reminder = new Reminder(mTitle, mDate, mTime);
        // Creating Reminder
        int ID = rb.addReminder(reminder);

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, mMonth - 1);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        Log.d(">>>notification-minute>", mMinute + "");
        // Create a new notification
        alarmReceiver.setAlarm(getApplicationContext(), mCalendar, ID);

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

    }
}
