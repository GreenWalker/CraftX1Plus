package me.floydz69.CraftX1Plus.util;

/**
 * Created by gusta on 01/05/2017.
 */
public class Prefix {

    private static String prefix = null;

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String pref) {
        prefix = Util.msg(pref);
    }
}
