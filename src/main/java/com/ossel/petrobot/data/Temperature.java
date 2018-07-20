package com.ossel.petrobot.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Temperature {

    private String value;

    private String time;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Temperature(String value) {
        super();
        this.value = value;
        this.time = dateFormat.format(new Date());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public String toCsvString() {
        return time + "," + value;
    }

}
