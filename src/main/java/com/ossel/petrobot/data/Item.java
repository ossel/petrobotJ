package com.ossel.petrobot.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {

    private String text;

    private Date createDate;

    private String creator;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


    public Item(String item, String creator) {
        super();
        this.text = item;
        this.creator = creator;
        createDate = new Date();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String toCsvString() {
        return dateFormat.format(createDate) + "," + text + "," + creator;
    }

    public static Item fromCsvString(String csvLine) {
        String[] s = csvLine.split(",");
        Item result = new Item(s[1], s[2]);
        try {
            result.setCreateDate(dateFormat.parse(s[0]));
        } catch (ParseException e) {
            e.printStackTrace();
            result.setCreateDate(new Date());
        }
        return result;
    }

}
