package me.floydz69.CraftX1Plus.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormat {

    public static String getData(){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        Date dataAtual = new Date(System.currentTimeMillis());
        return sd.format(dataAtual);
    }

}
