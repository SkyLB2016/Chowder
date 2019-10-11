package com.sky.oa.widget.calendar.common;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

public class CalendarInfo implements Parcelable {

    private int year, month, day;
    private CalendarMode mode;
    private Rect rect, borderRect;

    public CalendarInfo() {
    }

    public CalendarInfo(Parcel parcel) {
        this.year = parcel.readInt();
        this.month = parcel.readInt();
        this.day = parcel.readInt();
    }

    public CalendarInfo(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public CalendarInfo(int year, int month, int day, CalendarMode mode) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.mode = mode;
    }

    public CalendarInfo(Rect rect, int year, int month, int day, CalendarMode mode) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.mode = mode;
        this.rect = new Rect(rect);
    }

    public CalendarInfo(Rect rect, Rect borderRect, int year, int month, int day, CalendarMode mode) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.mode = mode;
        this.rect = new Rect(rect);
        this.borderRect = new Rect(borderRect);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public CalendarMode getMode() {
        return mode;
    }

    public Rect getRect() {
        return rect;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMode(CalendarMode mode) {
        this.mode = mode;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Rect getBorderRect() {
        return borderRect;
    }

    public void setBorderRect(Rect borderRect) {
        this.borderRect = borderRect;
    }

    public String getString() {
        switch (mode) {
            case YEAR:
                return "" + year;
            case MONTH:
                return year + "_" + month;
            case WEEK:
                return year + "_-" + month + "_" + day;
            case DAY:
                return year + "_" + month + "_" + day + "_data";
            default:
                break;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
    }

    public static final Creator<CalendarInfo> CREATOR = new Creator<CalendarInfo>() {
        @Override
        public CalendarInfo createFromParcel(Parcel parcel) {
            return new CalendarInfo(parcel);
        }

        @Override
        public CalendarInfo[] newArray(int size) {
            return new CalendarInfo[size];
        }
    };
}