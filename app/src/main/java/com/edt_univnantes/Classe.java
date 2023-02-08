package com.edt_univnantes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Classe implements Parcelable {

    public String DTSTART;
    public String DTEND;
    public String UID;
    public String SUMMARY;
    public String LOCATION;
    public String DESCRIPTION;
    public String CATEGORIES;

    public int year_start, month_start, day_start, hour_start, minute_start;
    public int year_stop, month_stop, day_stop, hour_stop, minute_stop;

    public Classe(String DTSTART, String DTEND, String UID, String SUMMARY, String LOCATION, String DESCRIPTION, String CATEGORIES) {
        super();
        this.DTSTART = Date_parser.toHumanReadable(DTSTART);
        this.DTEND = Date_parser.toHumanReadable(DTEND);
        this.UID = UID.replace("\\n", "\n");
        this.SUMMARY = SUMMARY.replace("\\n", "\n");
        this.LOCATION = LOCATION.replace("\\n", "\n");
        this.DESCRIPTION = DESCRIPTION.replace("\\n", "\n");
        this.CATEGORIES = CATEGORIES.replace("\\n", "\n");


        year_start  = Date_parser.getYear(DTSTART);
        month_start = Date_parser.getMonth(DTSTART);
        day_start   = Date_parser.getDay(DTSTART);
        hour_start  = Date_parser.getHour(DTSTART) + 1;
        minute_start= Date_parser.getMinute(DTSTART);

        year_stop   = Date_parser.getYear(DTEND);
        month_stop  = Date_parser.getMonth(DTEND);
        day_stop    = Date_parser.getDay(DTEND);
        hour_stop   = Date_parser.getHour(DTEND) + 1;
        minute_stop = Date_parser.getMinute(DTEND);
    }

    protected Classe(Parcel in) {
        DTSTART = in.readString();
        DTEND = in.readString();
        UID = in.readString();
        SUMMARY = in.readString();
        LOCATION = in.readString();
        DESCRIPTION = in.readString();
        CATEGORIES = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DTSTART);
        dest.writeString(DTEND);
        dest.writeString(UID);
        dest.writeString(SUMMARY);
        dest.writeString(LOCATION);
        dest.writeString(DESCRIPTION);
        dest.writeString(CATEGORIES);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Classe> CREATOR = new Creator<Classe>() {
        @Override
        public Classe createFromParcel(Parcel in) {
            return new Classe(in);
        }

        @Override
        public Classe[] newArray(int size) {
            return new Classe[size];
        }
    };

    @Override
    public String toString() {
        String res = "";
        res = "" + hour_start + ":" + minute_start + " - " + hour_stop + ":" + minute_stop + "\n" + DESCRIPTION;
        return res;
    }

    public String getDateFormatStr() {
        String res = "";

        res = "" + year_start + "/" + month_start + "/" + day_start + " " + hour_start + ":" + minute_start;

        return res;
    }


}