package com.ossel.petrobot.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.telegram.telegrambots.api.objects.User;
import com.ossel.petrobot.data.Item;
import com.ossel.petrobot.data.Person;
import com.ossel.petrobot.data.Request;
import com.ossel.petrobot.data.enums.BotCommand;

public class Util {

    public static String formatList(List<Item> list) {
        StringBuilder b = new StringBuilder();
        int i = 1;
        for (Item item : list) {
            b.append(i++);
            if (item.getText().startsWith(" ")) {
                b.append(".");
            } else {
                b.append(". ");
            }
            b.append(item.getText());
            b.append(" (");
            b.append(item.getCreator());
            b.append(")");
            b.append("\n");
        }
        return b.toString();
    }

    public static Request getRequestFromMessage(String message) {
        if (message != null) {
            if (message.startsWith("/todo_loesche") || message.startsWith("/todo_loeschen"))
                return new Request(BotCommand.DELETE_TODO_ITEM, getItemFromRawMessage(message));
            if (message.startsWith("/todolist"))
                return new Request(BotCommand.SHOW_TODO_LIST, "");
            if (message.startsWith("/todo"))
                return new Request(BotCommand.ADD_TODO_ITEM, getItemFromRawMessage(message));

            if (message.startsWith("/einkaufsliste_loeschen")
                    || message.startsWith("/einkaufliste_loeschen"))
                return new Request(BotCommand.DELETE_SHOPPING_LIST, "");
            if (message.startsWith("/einkaufsliste") || message.startsWith("/einkaufliste"))
                return new Request(BotCommand.SHOW_SHOPPING_LIST, "");
            if (message.startsWith("/einkauf"))
                return new Request(BotCommand.ADD_SHOPPING_ITEM, getItemFromRawMessage(message));

            if (message.startsWith("/pool"))
                return new Request(BotCommand.SHOW_POOL_TEMPERATURE, "");

            if (message.startsWith("/entendienst"))
                return new Request(BotCommand.SHOW_DUCK_FATHER, "");
            if (message.startsWith("/entenpapa") || message.startsWith("/entenmama"))
                return new Request(BotCommand.CLAIM_DUCK_RESPONSIBILITY, "");
            if (message.startsWith("/entenpunkte"))
                return new Request(BotCommand.SHOW_DUCK_STATS, "");

        }
        return new Request(BotCommand.UNKNOWN, "");
    }

    private static String getItemFromRawMessage(final String rawMessage) {
        String text = null;
        if (rawMessage.startsWith("/todo"))
            text = rawMessage.substring("/todo".length());
        if (rawMessage.startsWith("/todo_loesche"))
            text = rawMessage.substring("/todo_loesche".length());
        if (rawMessage.startsWith("/todo_loeschen"))
            text = rawMessage.substring("/todo_loeschen".length());
        if (rawMessage.startsWith("/einkauf"))
            text = rawMessage.substring("/einkauf".length());
        if (text.toLowerCase().startsWith("@petrobot")) {
            text = text.substring("@petrobot".length());
        }
        if (text.startsWith(" ")) {
            text = text.substring(1);
        }
        return text;
    }

    public static String getName(User user) {
        if (user.getUserName() != null) {
            return user.getUserName();
        }
        StringBuilder b = new StringBuilder();
        if (user.getFirstName() != null) {
            b.append(user.getFirstName());
        }
        if (user.getLastName() != null) {
            if (b.length() > 0) {
                b.append(" ");
            }
            b.append(user.getLastName());
        }
        return b.toString();
    }

    public static String toStatsString(Map<String, Integer> duckStats) {
        ArrayList<Person> orderedList = new ArrayList<>();
        for (String name : duckStats.keySet()) {
            orderedList.add(new Person(name, duckStats.get(name)));
        }
        Collections.sort(orderedList, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {

                return p1.getPoints() - p2.getPoints();
            }
        });
        StringBuilder result = new StringBuilder("Ranking:\n");
        int c = 1;
        for (Person person : orderedList) {
            if (c != 1)
                result.append("\n");
            result.append(c++);
            result.append(". ");
            result.append(person.getName());
            result.append(": ");
            result.append(person.getPoints());
        }
        return result.toString();
    }

    public static List<Item> toItemList(String message, String username) {
        ArrayList<Item> items = new ArrayList<>();
        for (String item : message.split(",")) {
            if (item != null && !item.isEmpty() && !item.equals(" "))
                if (item.startsWith(" ")) {
                    items.add(new Item(item.substring(1), username));
                } else {
                    items.add(new Item(item, username));
                }
        }
        return items;
    }

    public static boolean isToday(Date time) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(time);
        Calendar today = Calendar.getInstance();
        return targetTime.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && targetTime.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

}
