package com.ossel.petrobot.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ossel.petrobot.data.Item;

public class Dao {

    private static Dao instance;

    public static final String SHOPPING_LIST = "shopping-list.csv";
    public static final String TODO_LIST = "todo-list.csv";
    public static final String DUCK_FATHER_LIST = "duck-father-list.csv";

    private List<Item> todoList;
    private List<Item> shoppingList;
    private Map<String, Integer> duckStats;
    private String duckFather;
    private Date lastClaimTime;

    private Dao() {
        // TODO Auto-generated constructor stub
    }

    public static Dao getInstance() {
        if (instance == null)
            instance = new Dao();
        return instance;
    }

    public List<Item> getShoppingList() {
        if (shoppingList == null) {
            shoppingList = read(SHOPPING_LIST);
        }
        return shoppingList;
    }

    public void addShoppingItem(Item item) {
        if (shoppingList == null)
            shoppingList = getShoppingList();
        shoppingList.add(item);
        write(SHOPPING_LIST, toCsvString(shoppingList));
    }

    public void addShoppingItems(List<Item> itemList) {
        if (shoppingList == null)
            shoppingList = getShoppingList();
        shoppingList.addAll(itemList);
        write(SHOPPING_LIST, toCsvString(shoppingList));
    }

    public void deleteShoppingList() {
        if (shoppingList == null)
            shoppingList = getShoppingList();
        write(System.currentTimeMillis() + "-" + SHOPPING_LIST, toCsvString(shoppingList));
        shoppingList.clear();
        write(SHOPPING_LIST, " ");
    }

    public List<Item> getTodoList() {
        if (todoList == null) {
            todoList = read(TODO_LIST);
        }
        return todoList;
    }

    public void addTodoItem(Item item) {
        if (todoList == null)
            todoList = getTodoList();
        todoList.add(item);
        write(TODO_LIST, toCsvString(todoList));
    }

    public boolean deleteTodoItem(int itemNumber) {
        if (todoList == null)
            todoList = getTodoList();
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
        if (duckStats == null) {
            duckStats = readDuckPoints();
        }
        return duckStats;
    }

    public Map<String, Integer> setDuckFather(String username) {
        if (duckStats == null)
            duckStats = getDuckStats();
        duckFather = username;
        lastClaimTime = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
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
            Calendar today = Calendar.getInstance();
            Calendar claimTime = Calendar.getInstance();
            claimTime.setTime(lastClaimTime);
            if (claimTime.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                    && claimTime.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                return duckFather;
            }
        }
        return null;
    }


    private void write(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void append(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(content);
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
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
