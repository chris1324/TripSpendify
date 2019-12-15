package com.example.domain.Common.sharedvalueobject.date;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.result.Result;

import java.util.Calendar;

public class Date {

    // region Factory method -----------------------------------------------------------------------
    public Result<Date,Err.Create> create(long millisecond) {
        if (Check.isDefault(millisecond)) return Result.err(Err.Create.MILLISECOND_IS_ZERO);

        return Result.ok(new Date(millisecond));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
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

    public boolean isBefore(Date dateX) {
        int yearDif = this.getYear() - dateX.getYear();
        int monthDif = this.getMonth() - dateX.getMonth();
        int dateDif = this.getDayOfMonth() - dateX.getDayOfMonth();

        if (yearDif < 0) {
            return false;
        }

        if (yearDif == 0 && monthDif < 0) {
            return false;
        }

        if (yearDif == 0 && monthDif == 0 && dateDif < 0) {
            return false;
        }

        return true;

    }

    public boolean isSameDate(Date dateX) {
        return this.getYear() == dateX.getYear() &&
                this.getMonth() == dateX.getMonth() &&
                this.getDayOfMonth() == dateX.getDayOfMonth();
    }

    public boolean isAfter(Date dateX){
        return !isBefore(dateX);
    }

    // ---------------------------------------------------------------------------------------------

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month + 1;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    // ---------------------------------------------------------------------------------------------
    private Calendar getCalendar(long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(millisecond);
        return calendar;
    }


}