package sasd97.github.com.translator.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alexander on 10/04/2017.
 */

public class DateFormatter {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String prettyFormatDateTime(Date date) {
        SimpleDateFormat pretty = new SimpleDateFormat("MM.dd", Locale.getDefault());
        return pretty.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat(FORMAT, Locale.US);
        return iso8601Format.format(date);
    }

    public static Date fromString(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT, Locale.US);
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }
}
