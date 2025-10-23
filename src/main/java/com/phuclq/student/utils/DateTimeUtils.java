package com.phuclq.student.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtils {
    public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String ddMMyyyyHHmmSS = "dd/MM/yyyy HH:mm:ss";
    public static final String ddMMyyyyHHmm = "dd/MM/yyyy HH:mm";
    public static final String dd_MM_yyyy_HH_mm_SS = "dd_MM_yyyy_HH_mm_ss";
    public static final String HHmmSSddMMyyyy = "HH:mm:ss dd/MM/yyyy";
    public static final String DATE_TIME_MYSQL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SB_RESERVATION_DATE_FORMAT = "yyyy-M-dd HH:mm:ss";
    public static final String HHmmddMM = "HH:mm dd/MM";
    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String salesDateFormat = "yyyy/MM/dd";
    public static final String salesMonthFormat = "yyyy/MM";
    public static final String HHmmFormat = "HH:mm";
    public static final String yyyyMMddThhMMssZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String AMAZON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String MINUTE_START_FORMAT = "yyyy-MM-dd HH:mm:00";
    public static final String MINUTE_END_FORMAT = "yyyy-MM-dd HH:mm:59";
    public static final String saleSDateCheckoutFormat = "yyyy-MM-dd HH:mm";
    public static final String HHmmss = "HH:mm:ss";
    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String yyyyMM = "yyyyMM";
    public static final String yyyy_MM = "yyyy-MM";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String REGEX_YYYY_MM = "^\\d{4}-\\d{2}$";
    public static final String REGEX_YYYYMM = "^\\d{4}\\d{2}$";
    public static final String yyyy = "yyyy";
    public static final String MM = "MM";
    public static final String ZERO_HOUR = "00:00:00";
    public static final String ZERO_HHmm = "00:00";
    private static Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);

    public static String toDateString(Date date, String format) {
        if (date == null || StringUtils.isEmpty(format))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toDateStringFromTimeStamp(Timestamp date, String format) {
        if (date == null || StringUtils.isEmpty(format))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String convertStringFormatToOtherFormat(String dateStr, String firstFormat, String afterFormat) {
        if (StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(firstFormat) || StringUtils.isEmpty(afterFormat))
            return "";
        Date date = toDateFromStr(dateStr, firstFormat);
        return toDateString(date, afterFormat);
    }

    public static Date convertDateFromFormat(Date date, String format) {
        if (ObjectUtils.isEmpty(date) || StringUtils.isEmpty(format))
            return null;
        String strDateFirstFormat = toDateString(date, format);
        return toDateFromStr(strDateFirstFormat, format);
    }

    public static Date initDate(Integer year, Integer month, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static Date getCurrentTime() {
        return new Date();
    }

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getBeforeDate(int before) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -before);
        return cal.getTime();
    }

    public static Date getNextDate(int next) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +next);
        return cal.getTime();
    }

    /**
     * Now : truncated hour
     *
     * @return Timestamp
     */
    public static Timestamp before(long days) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days).truncatedTo(ChronoUnit.HOURS));
    }

    public static Date beginOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Timestamp beginOfDay(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Timestamp(cal.getTimeInMillis());
    }

    public static Date endOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String getCurrentTimeString(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Date toDateFromStr(String dateString, String format) {
        DateFormat dateTimeFormat = new SimpleDateFormat(format);
        try {
            Date date = dateTimeFormat.parse(dateString);
            return date;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static Timestamp toTimestampFromStr(String dateString, String format) {
        DateFormat dateTimeFormat = new SimpleDateFormat(format);
        try {
            Date date = dateTimeFormat.parse(dateString);
            return new java.sql.Timestamp(date.getTime());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<Date> getDatesBetweenTwoDates(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar) || calendar.equals(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static boolean isDateSame(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat(salesDateFormat);
        return fmt.format(date1).equals(fmt.format(date2));
    }

    public static String converTimeToString(Time time, String format) {
        Date date = new Date();
        date.setTime(time.getTime());
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return beginOfDay(cal.getTime());
    }

    public static Date getLastDatefMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endOfDay(cal.getTime());
    }

    public static boolean isValidFormat(String format, String value) {
        LocalDateTime ldt;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    logger.error(e.getMessage(), e);
                    // Debugging purposes
                    // e2.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Timestamp toTimeFromTime(Timestamp time, String format) {
        String date = toDateStringFromTimeStamp(time, format);
        return toTimestampFromStr(date, format);
    }

    public static int compareDate(Date date1, Date date2, String format) {
        String date1Str = toDateString(date1, format);
        Date dateOne = toDateFromStr(date1Str, format);
        String date2Str = toDateString(date2, format);
        Date dateTwo = toDateFromStr(date2Str, format);
        return dateOne.compareTo(dateTwo);
    }

    public static int compareDate(Date date1, Date date2) {
        return compareDate(date1, date2, salesDateFormat);
    }

    public static int compareDateMySql(Date date1, Date date2) {
        return compareDate(date1, date2, DATE_TIME_MYSQL_FORMAT);
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calenda = Calendar.getInstance();
        calenda.setTime(date);
        calenda.add(Calendar.HOUR_OF_DAY, hours);
        return calenda.getTime();
    }

    public static int hoursDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }

    public static Date subtractDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date getDateOneMonthAgo(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();
        return result;
    }

    public static Date getLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static Timestamp getDateMonthAgo(Timestamp date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();
        return new Timestamp(result.getTime());
    }

    public static Time valueOf(int hour, int minutes, int second) {
        return Time.valueOf(toLocalTime(hour, minutes, second));
    }

    public static LocalTime toLocalTime(int hour, int minutes, int second) {
        return LocalTime.of(hour, minutes, second);
    }

    /**
     * convert from Date to LocalTime
     *
     * @param date
     * @return value LocalTime (hh:mm)
     */
    public static LocalTime convertDate2LocalTime(Date date, String format) {
        LocalDateTime localDateTime = new Timestamp(convertDateFromFormat(date, format).getTime()).toLocalDateTime();
        return localDateTime.toLocalTime();
    }

    /**
     * convert from string to LocalTime by format
     *
     * @param value
     * @param format
     * @return LocalTime by format
     */
    public static LocalTime convertString2LocalTime(String value, String format) {
        return LocalTime.parse(value, DateTimeFormatter.ofPattern(format));
    }

    public static int getHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    public static int getSeconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.SECOND);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static Timestamp getCurrentTimeMillis() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp addDays(long days, Timestamp t1) throws Exception {
        if (days < 0) {
            throw new Exception("Day in wrong format.");
        }
        Long milliseconds = days * 24 * 60 * 60 * 1000;
        return new Timestamp(t1.getTime() + milliseconds);
    }

    public static Timestamp addHours(long hours, Timestamp t1) throws Exception {
        if (hours < 0) {
            throw new Exception("Day in wrong format.");
        }
        Long milliseconds = hours * 60 * 60 * 1000;
        return new Timestamp(t1.getTime() + milliseconds);
    }

    /**
     * check time1 less than time2
     *
     * @param time1
     * @param time2
     * @return true if time1 < t2
     */
    public static boolean isBeforeHour(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2);
    }

    /**
     * check time1 bigger time
     *
     * @param time1
     * @param time2
     * @return true time1 > time2
     */
    public static boolean isAfterHour(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2);
    }

    public static Time toTimeSQL(String value) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(value, DateTimeFormatter.ofPattern(HHmmFormat));
        return Time.valueOf(localTime);
    }

    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static LocalTime convertTimeToLocalTime(Time time, String format) {
        return LocalTime.parse(time.toString(), DateTimeFormatter.ofPattern(format));
    }

    public static boolean isValidateFormat(String format, String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return isValidFormat(format, value);
    }

    public static Timestamp getStartOfNextDay(Timestamp timestamp) {
        if (timestamp != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp.getTime());
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return new Timestamp(cal.getTimeInMillis());
        }
        return null;
    }

    public static boolean checkSameLocalTime(LocalTime time1, LocalTime time2) {
        return (time1.compareTo(time2) == 0) ? true : false;
    }

    public static Date plusDay(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, amount);
        return c.getTime();
    }

    public static Date parseMysqlDatetime(String value) {
        if (!StringUtils.isEmpty(value) && isValidFormat(DATE_TIME_MYSQL_FORMAT, value)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_MYSQL_FORMAT);
                return sdf.parse(value);
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
                logger.debug("{} is not mysql datetime format.", value);
            }
        }
        return null;
    }

    public static String toMysql(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_MYSQL_FORMAT);
        return sdf.format(date);
    }

    public static String toMysql(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_MYSQL_FORMAT);
        return sdf.format(time);
    }

    public static Date parseSbReservationDate(String value) {
        if (!StringUtils.isEmpty(value) && isValidFormat(SB_RESERVATION_DATE_FORMAT, value)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(SB_RESERVATION_DATE_FORMAT);
                return sdf.parse(value);
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static boolean isValidateMonth(String month) {
        if (StringUtils.isEmpty(month))
            return false;
        String number = "[0-9]*";
        Pattern pattern = Pattern.compile(number);
        Matcher matcher = pattern.matcher(month);
        if (!matcher.matches() || Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12 || month.length() != 2) {
            return false;
        }
        return true;
    }

    public static Date setHoursForDate(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    public static Date setMinutesForDate(Date date, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    public static Date setSecondsForDate(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, seconds);
        return c.getTime();
    }

    public static Date setSecondsForDate(Date date, int seconds, int ms) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, seconds);
        c.set(Calendar.MILLISECOND, ms);
        return c.getTime();
    }

    public static Date setTimeForDate(Date date, int hours, int minutes, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, seconds);
        return c.getTime();
    }

    public static boolean isValidDate(String date, String regex, String format) {
        if (ObjectUtils.isEmpty(date) || !date.matches(regex)) {
            logger.info("not match parttern.");
            return false;
        }

        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        try {
            df.parse(date);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static Date plusMinutes(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, amount);
        return c.getTime();
    }

    public static Date plusMonth(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, amount);
        return c.getTime();
    }

    public static int getDayOfWeekFromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isValidateYear(String year) {
        if (StringUtils.isEmpty(year))
            return false;
        String number = "[0-9]*";
        Pattern pattern = Pattern.compile(number);
        Matcher matcher = pattern.matcher(year);
        if (!matcher.matches() || Integer.parseInt(year) < 1000 || year.length() != 4) {
            return false;
        }
        return true;
    }

    public static boolean isValidateByFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            System.out.println(value);
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return toDateString(date1, yyyyMMdd).equals(toDateString(date2, yyyyMMdd));
    }

    public static String converZoneDateTime2String(ZonedDateTime time, String format) {
        if (time == null || StringUtils.isEmpty(format)) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return time.format(formatter);
    }

    public static Timestamp timeNow() {
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);
        return timestamp;
    }

    public static Date convertStringDateYYmmdd(String value) {
        return convertString2Date(value, DateTimeUtils.yyyy_MM_dd);
    }

    public static Date convertString2Date(String value, String format) {
        return value == null ? null : toDateFromStr(value, format);
    }

    public static Timestamp convertDateToTimestamp(Date date) {

        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }

    public static String convertDateTimeToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }
}

    
