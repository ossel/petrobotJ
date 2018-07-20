package com.ossel.petrobot.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ossel.petrobot.data.Item;
import com.ossel.petrobot.data.Temperature;
import com.ossel.petrobot.utility.Util;

public class Dao {

    private static Dao instance;

    public static final String SHOPPING_LIST = "shopping-list.csv";
    public static final String TODO_LIST = "todo-list.csv";
    public static final String DUCK_FATHER_LIST = "duck-father-list.csv";
    public static final String TEMPERATURE_LIST = "temperature.csv";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<Item> todoList;
    private List<Item> shoppingList;
    private Map<String, Integer> duckStats;
    private String duckFather;
    private Date lastClaimTime;
    private Temperature temperature;

    private Dao() {
        shoppingList = read(SHOPPING_LIST);
        todoList = read(TODO_LIST);
        duckStats = readDuckPoints();
    }

    public static Dao getInstance() {
        if (instance == null)
            instance = new Dao();
        return instance;
    }

    public List<Item> getShoppingList() {
        return shoppingList;
    }

    public void addShoppingItem(Item item) {
        shoppingList.add(item);
        write(SHOPPING_LIST, toCsvString(shoppingList));
    }

    public void addShoppingItems(List<Item> itemList) {
        shoppingList.addAll(itemList);
        write(SHOPPING_LIST, toCsvString(shoppingList));
    }

    public void deleteShoppingList() {
        write(System.currentTimeMillis() + "-" + SHOPPING_LIST, toCsvString(shoppingList));
        shoppingList.clear();
        write(SHOPPING_LIST, " ");
    }

    public List<Item> getTodoList() {
        return todoList;
    }

    public void addTodoItem(Item item) {
        todoList.add(item);
        write(TODO_LIST, toCsvString(todoList));
    }

    public boolean deleteTodoItem(int itemNumber) {
        try {
            todoList.remove(itemNumber - 1);
            write(TODO_LIST, toCsvString(todoList));
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getDuckStats() {
        return duckStats;
    }

    public Map<String, Integer> setDuckFather(String username) {
        duckFather = username;
        lastClaimTime = new Date();

        String strDate = dateFormat.format(lastClaimTime);
        append(DUCK_FATHER_LIST, strDate + "," + duckFather);
        // add duck point
        if (duckStats.containsKey(username)) {
            duckStats.put(username, duckStats.get(username) + 1);
        } else {
            duckStats.put(username, 1);
        }
        return duckStats;
    }

    public String getDuckFather() {
        if (lastClaimTime != null) {
            if (Util.isToday(lastClaimTime)) {
                return duckFather;
            }
        }
        return null;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void putTemperature(String value) {
        temperature = new Temperature(value);
        append(TEMPERATURE_LIST, temperature.toCsvString());
    }


    private void write(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * appends the content and a new line to the requested file.
     * 
     * @param filename
     * @param content
     */
    private void append(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(content + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String toCsvString(List<Item> items) {
        StringBuilder b = new StringBuilder();
        for (Item item : items) {
            b.append(item.toCsvString());
            b.append("\n");
        }
        return b.toString();
    }

    private List<Item> read(String filename) {
        List<Item> res = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine != null && !sCurrentLine.isEmpty() && !sCurrentLine.equals(" ")) {
                    res.add(Item.fromCsvString(sCurrentLine));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Map<String, Integer> readDuckPoints() {
        Map<String, Integer> res = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DUCK_FATHER_LIST))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine != null && !sCurrentLine.isEmpty() && !sCurrentLine.equals(" ")) {
                    String username = sCurrentLine.split(",")[1];
                    if (res.containsKey(username)) {
                        res.put(username, res.get(username) + 1);
                    } else {
                        res.put(username, 1);
                    }
                    try {
                        lastClaimTime = dateFormat.parse(sCurrentLine.split(",")[0]);
                        if (Util.isToday(lastClaimTime))
                            duckFather = username;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }



}
