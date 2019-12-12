package com.iCropal.iPhobia.Utility.Resources;

import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

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
                "Thursday", "Friday", "Saturday", "Sunday"};
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(date));
            return daysList[c.get(Calendar.DAY_OF_WEEK) -2] + ", " + time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getUserAge(String dob) {
        Date d;
        try {
            d = longFormat.parse(dob);
            Calendar a = getCalendar(d);
            Calendar b = Calendar.getInstance();
            int diff = b.get(YEAR) - a.get(YEAR);
            if (a.get(MONTH) > b.get(MONTH) ||
                    (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
                diff--;
            }
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static Calendar getCalendar(Date date) {
        Calendar x = Calendar.getInstance();
        x.setTime(date);
        return x;
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

    public static String getYear(DatePicker datePicker) {
        Calendar c = Calendar.getInstance();
        c.set(MONTH, datePicker.getMonth());
        c.set(YEAR, datePicker.getYear());
        c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        return longFormat.format(c.getTime());
    }

    public static Calendar getDate(String recordDate, String recordTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String date = recordDate + " " + recordTime;
        calendar.setTime(format.parse(date));
        return calendar;
    }
}
