package com.ossel.petrobot.api;

import java.util.ArrayList;
import java.util.List;
import com.ossel.petrobot.data.Item;
import com.ossel.petrobot.data.Poll;
import com.ossel.petrobot.data.enums.BotCommand;

public class TelegramRequest {

    private String from; // username

    final private String rawMessage; // command + message

    private String message;

    private BotCommand command;

    private List<Item> items;

    private Poll poll;

    public TelegramRequest(String rawMessage, String username) {
        super();
        this.from = username;
        this.rawMessage = rawMessage;
        this.command = BotCommand.UNKNOWN;
        if (rawMessage != null) {
            if (rawMessage.toLowerCase().startsWith("/todo_loeschen")) {
                this.command = BotCommand.DELETE_TODO_ITEM;
                this.message = removeCommandPrefix(rawMessage, "/todo_loeschen");
            } else if (rawMessage.startsWith("/todo_loesche")) {
                this.command = BotCommand.DELETE_TODO_ITEM;
                this.message = removeCommandPrefix(rawMessage.toLowerCase(), "/todo_loesche");
            } else if (rawMessage.toLowerCase().startsWith("/todoliste")) {
                this.command = BotCommand.SHOW_TODO_LIST;
                this.message = removeCommandPrefix(rawMessage, "/todoliste");
            } else if (rawMessage.toLowerCase().startsWith("/todolist")) {
                this.command = BotCommand.SHOW_TODO_LIST;
                this.message = removeCommandPrefix(rawMessage, "/todolist");
            } else if (rawMessage.toLowerCase().startsWith("/todo")) {
                this.command = BotCommand.ADD_TODO_ITEM;
                this.message = removeCommandPrefix(rawMessage, "/todo");
                this.items = getItemListFromMessage(message);
            } else if (rawMessage.toLowerCase().startsWith("/einkaufsliste_loeschen")) {
                this.command = BotCommand.DELETE_SHOPPING_LIST;
                this.message = removeCommandPrefix(rawMessage, "/einkaufsliste_loeschen");
            } else if (rawMessage.toLowerCase().startsWith("/einkaufliste_loeschen")) {
                this.command = BotCommand.DELETE_SHOPPING_LIST;
                this.message = removeCommandPrefix(rawMessage, "/einkaufliste_loeschen");
            } else if (rawMessage.toLowerCase().startsWith("/einkaufsliste")) {
                this.command = BotCommand.SHOW_SHOPPING_LIST;
                this.message = removeCommandPrefix(rawMessage, "/einkaufsliste");
            } else if (rawMessage.toLowerCase().startsWith("/einkaufliste")) {
                this.command = BotCommand.SHOW_SHOPPING_LIST;
                this.message = removeCommandPrefix(rawMessage, "/einkaufliste");
            } else if (rawMessage.toLowerCase().startsWith("/einkauf")) {
                this.command = BotCommand.ADD_SHOPPING_ITEM;
                this.message = removeCommandPrefix(rawMessage, "/einkauf");
                this.items = getItemListFromMessage(this.message);
            } else if (rawMessage.toLowerCase().startsWith("/pool")) {
                this.command = BotCommand.SHOW_POOL_TEMPERATURE;
                this.message = removeCommandPrefix(rawMessage, "/pool");
            } else if (rawMessage.toLowerCase().startsWith("/entendienst")) {
                this.command = BotCommand.SHOW_DUCK_FATHER;
                this.message = removeCommandPrefix(rawMessage, "/entendienst");
            } else if (rawMessage.toLowerCase().startsWith("/entenpapa")) {
                this.command = BotCommand.CLAIM_DUCK_RESPONSIBILITY;
                this.message = removeCommandPrefix(rawMessage, "/entenpapa");
            } else if (rawMessage.toLowerCase().startsWith("/entenmama")) {
                this.command = BotCommand.CLAIM_DUCK_RESPONSIBILITY;
                this.message = removeCommandPrefix(rawMessage, "/entenmama");
            } else if (rawMessage.toLowerCase().startsWith("/entenpunkte")) {
                this.command = BotCommand.SHOW_DUCK_STATS;
                this.message = removeCommandPrefix(rawMessage, "/entenpunkte");
            } else if (rawMessage.toLowerCase().startsWith("/umfrage_fertig")) {
                this.command = BotCommand.POLL_FINISHED;
                this.message = removeCommandPrefix(rawMessage, "/umfrage_fertig");
            } else if (rawMessage.toLowerCase().startsWith("/umfrage")) {
                this.command = BotCommand.POLL;
                this.message = removeCommandPrefix(rawMessage, "/umfrage");
                this.poll = getPollFromMessage(this.message);
            } else if (rawMessage.toLowerCase().startsWith("/debug")) {
                this.command = BotCommand.DEBUG;
                this.message = removeCommandPrefix(rawMessage, "/debug");
            } else if (rawMessage.toLowerCase().startsWith("/start")) {
                this.command = BotCommand.IGNORE_COMMAND;
                this.message = rawMessage;
            } else if (Character.isDigit(rawMessage.charAt(1))) {
                this.command = BotCommand.POLL_VOTE;
                this.message = String.valueOf(rawMessage.charAt(1));
            }

        }
    }

    private Poll getPollFromMessage(String message) {
        Poll poll = null;
        if (message != null && message.contains("?") && message.contains(",")) {
            String[] s = message.split("\\?");
            poll = new Poll(s[0] + "?");
            for (String option : message.substring(poll.getQuestion().length()).split(",")) {
                if (option.startsWith(" ")) {
                    option.substring(1);
                }
                if (!option.isEmpty())
                    poll.addOption(option);
            }
        }
        return poll;
    }

    private static String removeCommandPrefix(String message, String prefix) {
        String result = message;
        if (result.startsWith(prefix)) {
            result = result.substring(prefix.length());
        }
        if (result.toLowerCase().startsWith("@petrobot")) {
            result = result.substring("@petrobot".length());
        }
        if (result.startsWith(" ")) {
            result = result.substring(1);
        }
        return result;
    }

    private List<Item> getItemListFromMessage(final String message) {
        String text = message;
        List<Item> items = new ArrayList<>();
        if (text.contains(",")) {
            for (String item : text.split(",")) {
                if (item != null && !item.isEmpty() && !item.equals(" "))
                    if (item.startsWith(" ")) {
                        items.add(new Item(item.substring(1), this.from));
                    } else {
                        items.add(new Item(item, this.from));
                    }
            }
        } else {
            items.add(new Item(text, this.from));
        }
        return items;
    }


    public BotCommand getCommand() {
        return command;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public String toString() {
        return "Request [from=" + from + ",\nrawMessage=" + rawMessage + ",\ncommand=" + command
                + ", message=" + message + ",\nitems=" + items + ", poll=" + poll + "]";
    }



}
