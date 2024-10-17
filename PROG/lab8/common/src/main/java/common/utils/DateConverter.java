package common.utils;

import common.exceptions.InvalidDateFormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Provides methods to convenient conversion between String and Date
 */
public class DateConverter {
    private static String pattern = "yyyy-MM-dd";
    private static DateFormat dateFormatter = new SimpleDateFormat(pattern);
    private static DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(pattern);
    public static void setPattern(String p){
        pattern = p;
        dateFormatter = new SimpleDateFormat(pattern);
        localDateFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * convert Date to String
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return date!=null?dateFormatter.format(date):null;
    }

    /**
     * convert LocalDate to String
     *
     * @param date
     * @return
     */
    public static String dateToString(LocalDate date) {
        return date!=null?date.format(localDateFormatter):null;
    }

    /**
     * convert LocalDate to String
     *
     * @param s
     * @return
     * @throws InvalidDateFormatException
     */
    public static LocalDate parseLocalDate(String s) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(s, localDateFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }

    /**
     * convert Date to String
     *
     * @param s
     * @return String
     * @throws InvalidDateFormatException
     */
    public static Date parseDate(String s) throws InvalidDateFormatException {
        try {
            return dateFormatter.parse(s);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
