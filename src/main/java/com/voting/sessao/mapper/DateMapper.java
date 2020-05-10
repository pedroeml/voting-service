package com.voting.sessao.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateMapper {
    public static final String datePattern = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    public static Date mapFrom(String date) throws ParseException {
        return dateFormatter.parse(date);
    }

    public static String mapToString(Date date) {
        return dateFormatter.format(date);
    }

    public static Date mapToFutureDate(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);

        return cal.getTime();
    }
}
