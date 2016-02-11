package myapp.sample.com.reminder;

public class Reminder {
    private int mID;
    private String mTitle;
    private String mDate;
    private String mTime;


    public Reminder(int ID, String Title, String Date, String Time){
        mID = ID;
        mTitle = Title;
        mDate = Date;
        mTime = Time;
    }

    public Reminder(String Title, String Date, String Time){
        mTitle = Title;
        mDate = Date;
        mTime = Time;
    }

    public Reminder(){}

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

}
