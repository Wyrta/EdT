package com.edt_univnantes;

public class Agenda {

    public int year, month, day;
    public String description;

    public Agenda(int myear, int mmonth, int mday, String mdescrition) {
        year        = myear;
        month       = mmonth;
        day         = mday;
        description = mdescrition;
    }

    public String toString() {
        String res = "";

        res = "" + year + "/" + month + "/" + day + " : " + description;

        return res;
    }

    static public String toData(int myear, int mmonth, int mday, String mdescrition) {
        String res = "";

        res = "" + myear + ";" + mmonth + ";" + mday + ";" + mdescrition + "; ";

        return res;
    }

    static public Agenda parseStr(String str, String separator) {
        int year = 0;
        int month = 0;
        int day = 0;
        String description = "empty";
        int idx, lastIdx;

        lastIdx = 0;
        idx = str.indexOf(separator, lastIdx);


        if (idx <= 0) {
            return null;

        } else {
            year = Integer.parseInt(str.substring(lastIdx, idx));

            lastIdx = idx;
            idx = str.indexOf(separator, lastIdx + 1);
            month = Integer.parseInt(str.substring(lastIdx + 1, idx));

            lastIdx = idx;
            idx = str.indexOf(separator, lastIdx + 1);
            day = Integer.parseInt(str.substring(lastIdx + 1, idx));

            lastIdx = idx;
            idx = str.indexOf(separator, lastIdx + 1);
            description = str.substring(lastIdx + 1, idx);

            return new Agenda(year, month, day, description);
        }
    }

    static public Agenda parseStr(String str) {
            int year = 0;
            int month = 0;
            int day = 0;
            String description = "empty";
            int idx, lastIdx;

            lastIdx = 0;
            idx = str.indexOf(";", lastIdx);

            if (idx <= 0) {
                return null;

            } else {
                year = Integer.parseInt(str.substring(lastIdx, idx));

                lastIdx = idx;
                idx = str.indexOf(";", lastIdx + 1);
                month = Integer.parseInt(str.substring(lastIdx + 1, idx));

                lastIdx = idx;
                idx = str.indexOf(";", lastIdx + 1);
                day = Integer.parseInt(str.substring(lastIdx + 1, idx));

                lastIdx = idx;
                idx = str.indexOf(";", lastIdx + 1);
                description = str.substring(lastIdx + 1, idx);

                return new Agenda(year, month, day, description);
            }

        }

}
