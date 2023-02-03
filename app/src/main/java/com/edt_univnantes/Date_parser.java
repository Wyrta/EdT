package com.edt_univnantes;

public class Date_parser {
    static public String toHumanReadable(String date) {
        String res = "";

        res = "" + date.charAt(9) + date.charAt(10) + ":" + date.charAt(11) + date.charAt(12);

        return res;
    }

    static public String toMachinReadable(String date) {
        String res = "";

        res = date.charAt(0) + date.charAt(1) + date.charAt(2) + date.charAt(3) + "-";


        return res;
    }
    static public int getYear(String date)  {
        int res;
        String resStr = "" + date.charAt(0) + date.charAt(1) + date.charAt(2) + date.charAt(3);
        res = Integer.parseInt(resStr);
        return res;
    }
    static public int getMonth(String date) {
        int res;
        String resStr = "" + date.charAt(4) + date.charAt(5);
        res = Integer.parseInt(resStr);
        return res;
    }
    static public int getDay(String date)   {
        int res;
        String resStr = "" + date.charAt(6) + date.charAt(7);
        res = Integer.parseInt(resStr);
        return res;
    }

    static public int getHour(String date)   {
        int res;
        String resStr = "" + date.charAt(9) + date.charAt(10);
        res = Integer.parseInt(resStr);
        return res;
    }

    static public int getMinute(String date)   {
        int res;
        String resStr = "" + date.charAt(11) + date.charAt(12);
        res = Integer.parseInt(resStr);
        return res;
    }
}
