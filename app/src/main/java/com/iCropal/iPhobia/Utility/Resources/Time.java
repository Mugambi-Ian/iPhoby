package com.iCropal.iPhobia.Utility.Resources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {
    private static SimpleDateFormat _12HrTime = new SimpleDateFormat("hh:mm aa");
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat longFormat = new SimpleDateFormat("dd MMMM, yyyy");

    public static String getDate() {
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static String getTime() {
        return _12HrTime.format(Calendar.getInstance().getTime());
    }

    public static String myTime(String date, String time) {
        String daysList[] = {"Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday","Sunday"};
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(date));
            return daysList[c.get(Calendar.DAY_OF_WEEK)-1] + ", " + time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLongDate(String recordDate) {
        Calendar x = Calendar.getInstance();
        try {
            x.setTime(dateFormat.parse(recordDate));
            return longFormat.format(x.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
