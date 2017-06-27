package com.graphicodeci.address.helper;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by MARTIAL ANOUMAN on 6/24/2017.
 * @author Martial Anouman
 */
public class DateHelper {

    /**
     * The date format use for conversion
     */
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /**
     * The date formatter
     */
    private static final DateTimeFormatter DATE_FORMATTER= DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Return the localDate formatted as a String with the pattern {DateHelper#DATE_PATTERN}
     *
     * @param localDate The date to be returned as a String
     *
     * @return formatted string
     */
    public static String format(LocalDate localDate){
        if(localDate == null) return null;

        return DATE_FORMATTER.format(localDate);
    }

    /**
     * Return a LocalDte from given date string according to {DateHelper#DATE_PATTERN} format
     * @param dateString string to be parse
     * @return LocalDate from given string. Null if string can't be converted
     */
    public static LocalDate parse(String dateString){

        try {
            return DATE_FORMATTER.parse(dateString,LocalDate::from);
        } catch (DateTimeParseException e) {
            return  null;
        }
    }

    public static boolean validDate(String dateString){
        //Try to parse
        return DateHelper.parse(dateString) != null ;
    }
}
