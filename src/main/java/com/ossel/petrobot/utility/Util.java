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

public class Util {

    public static String formatList(List<Item> list) {
        StringBuilder b = new StringBuilder();
        int i = 1;
        for (Item item : list) {
            if (i < 10) {
                b.append("0");
            }
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

                return p2.getPoints() - p1.getPoints();
            }
        });
        StringBuilder result = new StringBuilder("Entenpunkte:\n");
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

    public static boolean isToday(Date time) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(time);
        Calendar today = Calendar.getInstance();
        return targetTime.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && targetTime.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

}
