package com.example.domain.Shared.valueObject.date;

import com.example.domain.Shared.errorhanding.answer.Answer;
import com.example.domain.Shared.errorhanding.check.Check;
import com.example.domain.Shared.errorhanding.result.Result;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Objects;

public class Date {

    // region Factory method -----------------------------------------------------------------------
    public static Result<Date, Err.Create> create(long millisecond) {
        if (Check.isDefault(millisecond)) return Result.err(Err.Create.MILLISECOND_IS_ZERO);

        return Result.ok(new Date(millisecond));
    }

    public static Date today() {
        return new Date(Calendar.getInstance().getTimeInMillis());
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            MILLISECOND_IS_ZERO
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final long millisecond;
    private final int year;
    private final int month;
    private final int dayOfMonth;

    private Date(long millisecond) {
        this.millisecond = millisecond;
        Calendar calendar = getCalendar(millisecond);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DATE);
    }
    // endregion Variables and Constructor ---------------------------------------------------------


    // --------------------------------------- Comparing -------------------------------------------

    public enum CompareTo {
        IS_BEFORE,
        SAME_DAY,
        IS_AFTER
    }

    public Answer<CompareTo> compareTo(Date dateX) {
        int yearDif = this.getYear() - dateX.getYear();
        int monthDif = this.getMonth() - dateX.getMonth();
        int dateDif = this.getDayOfMonth() - dateX.getDayOfMonth();

        if (yearDif < 0) return Answer.make(CompareTo.IS_BEFORE);
        if (yearDif == 0 && monthDif < 0) return Answer.make(CompareTo.IS_BEFORE);
        if (yearDif == 0 && monthDif == 0 && dateDif < 0) return Answer.make(CompareTo.IS_BEFORE);
        if (yearDif == 0 && monthDif == 0 && dateDif == 0) return Answer.make(CompareTo.SAME_DAY);

        return Answer.make(CompareTo.IS_AFTER);
    }

    public boolean isBefore(Date dateX) {
        return (this.compareTo(dateX).getAnswer() == CompareTo.IS_BEFORE);
    }

    public boolean isSameDate(Date dateX) {
        return (this.compareTo(dateX).getAnswer() == CompareTo.SAME_DAY);
    }

    public boolean isAfter(Date dateX) {
        return (this.compareTo(dateX).getAnswer() == CompareTo.IS_AFTER);
    }

    public int differentInDay(Date dateX) {
        long difference = Math.abs(this.getMillisecond() - dateX.getMillisecond());
        long differenceDay = difference / (24 * 60 * 60 * 1000);
        return BigDecimal.valueOf(differenceDay).setScale(0, BigDecimal.ROUND_UP).intValue();
    }


    // ----------------------------------- Hash and Equals -----------------------------------------
    @Override
    public int hashCode() {
        return Objects.hash(year, month, dayOfMonth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Date date = (Date) o;
        return year == date.year &&
                month == date.month &&
                dayOfMonth == date.dayOfMonth;
    }

    // region Getter -------------------------------------------------------------------------------

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return getMonthZeroBase() + 1;
    }

    public int getMonthZeroBase() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public long getMillisecond() {
        return millisecond;
    }
    // endregion Getter ----------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    private Calendar getCalendar(long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(millisecond);
        return calendar;
    }
    // endregion helper method ---------------------------------------------------------------------

    // region DateComparator -----------------------------------------------------------------------
    public static Comparator<Date> getComparator() {
        return new DateComparator();
    }

    private static class DateComparator implements Comparator<Date> {

        @Override
        public int compare(Date date1, Date date2) {
            Date.CompareTo answer = date1.compareTo(date2).getAnswer();
            if (answer == Date.CompareTo.IS_AFTER) return 1;
            if (answer == Date.CompareTo.IS_BEFORE) return -1;
            if (answer == Date.CompareTo.SAME_DAY) return 0;

            return 0;
        }
    }
    // endregion DateComparator --------------------------------------------------------------------


}
