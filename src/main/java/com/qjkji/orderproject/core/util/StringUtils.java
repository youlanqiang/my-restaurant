package com.qjkji.orderproject.core.util;

import com.qjkji.orderproject.core.exception.UnmessageException;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils {

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static String formatDateTime(LocalDateTime dateTime){
        if(dateTime == null){
            throw new UnmessageException("");
        }
        return dateTimeFormatter.format(dateTime);
    }

    public static String formatDate(LocalDate date){
        if(date == null){
            throw new UnmessageException("");
        }
        return dateFormatter.format(date);
    }

    public static String formatPrice(BigDecimal price){
        if(price == null){
            return "0.00";
        }
        return decimalFormat.format(price);
    }
}
