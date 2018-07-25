package com.ossel.petrobot;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.ossel.petrobot.data.Item;
import com.ossel.petrobot.data.Request;
import com.ossel.petrobot.data.Temperature;
import com.ossel.petrobot.services.Dao;
import com.ossel.petrobot.utility.Util;

public class PetroBot extends TelegramLongPollingBot {

    private static final Logger LOG = Logger.getLogger(PetroBot.class);

    private String CHAT_ID;
    private String API_TOKEN;

    private Dao dao = Dao.getInstance();

    PetroBot(String token, String chatId) {
        CHAT_ID = chatId;
        API_TOKEN = token;
    }

    public String getBotUsername() {
        return "PetroBot";
    }

    public void onUpdateReceived(Update data) {
        System.out.println("Update: " + data.toString());
        Request request = Util.getRequestFromMessage(data.getMessage().getText());
        String username = Util.getName(data.getMessage().getFrom());
        LOG.info(request.toString() + "\nRaw: " + data.getMessage().getText());
        switch (request.getCommand()) {
            case SHOW_SHOPPING_LIST: {
                if (dao.getShoppingList().isEmpty()) {
                    sendMessage(
                            "Einkaufsliste leer. Tippe '/einkauf <item>' um etwas hinzuzufügen.");
                } else {
                    sendMessage(Util.formatList(dao.getShoppingList()));
                }
                break;
            }
            case ADD_SHOPPING_ITEM: {
                if (request.getItem().contains(",")) {
                    dao.addShoppingItems(Util.toItemList(request.getItem(), username));
                } else {
                    dao.addShoppingItem(new Item(request.getItem(), username));
                }
                break;
            }
            case DELETE_SHOPPING_LIST: {
                dao.deleteShoppingList();
                sendMessage("Einkaufsliste gelöscht.");
                break;
            }
            case SHOW_POOL_TEMPERATURE: {
                Temperature temp = dao.getTemperature();
                if (temp == null) {
                    sendMessage("Der Sensor ist nicht angeschlossen.");
                } else {
                    sendMessage("Die Pooltemperatur beträgt " + temp.getValue() + " Grad. ("
                            + temp.getTime() + ")");
                }
                break;
            }
            case SHOW_TODO_LIST: {
                if (dao.getTodoList().isEmpty()) {
                    sendMessage("Todo-Liste leer. Tippe '/todo <item>' um etwas hinzuzufügen.");
                } else {
                    sendMessage(Util.formatList(dao.getTodoList()));
                }
                break;
            }
            case ADD_TODO_ITEM: {
                dao.addTodoItem(new Item(request.getItem(), username));
                break;
            }
            case DELETE_TODO_ITEM: {
                try {
                    int itemNumber = Integer.parseInt(request.getItem().trim());
                    boolean deleted = dao.deleteTodoItem(itemNumber);
                    if (deleted)
                        sendMessage("Item <" + itemNumber + "> gelöscht.");
                    else
                        sendMessage("Item <" + itemNumber + "> konnte nicht gelöscht werden.");
                } catch (NumberFormatException e) {
                    sendMessage("Der Wert <" + request.getItem().trim()
                            + "> ist keine positive ganze Zahl.");
                }
                break;
            }
            case SHOW_DUCK_STATS: {
                if (dao.getDuckStats().isEmpty()) {
                    sendMessage(
                            "Noch keine Entenpunkte vorhanden. Tippe /entenpapa oder /entenmama um den Entendienst zu übernehmen und Entenpunkte zu sammeln.");
                } else {
                    sendMessage(Util.toStatsString(dao.getDuckStats()));
                }
                break;
            }
            case SHOW_DUCK_FATHER: {
                if (dao.getDuckFather() == null) {
                    sendMessage(
                            "Heute ist noch niemand für die Enten zuständig. Tippe /entenpapa oder /entenmama um den Entendienst zu übernehmen und Entenpunkte zu sammeln.");
                } else {
                    sendMessage(dao.getDuckFather() + " ist heute für die Enten zuständig.");
                }
                break;
            }
            case CLAIM_DUCK_RESPONSIBILITY: {
                if (dao.getDuckFather() != null) {
                    sendMessage(dao.getDuckFather()
                            + " ist heute bereits für die Enten zuständig. Versuche es morgen erneut.");
                    break;
                }
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                if (hour < 18) {
                    sendMessage(
                            "Die Enten müssen noch nicht ins Bett. Versuche es ab 18:00 erneut!");
                } else {
                    Map<String, Integer> duckStats = dao.setDuckFather(username);
                    if (duckStats.get(username) != 1) {
                        sendMessage(username + " ist heute für die Enten zuständig und hat nun "
                                + duckStats.get(username) + " Entenpunkte.");
                    } else {
                        sendMessage(username
                                + " ist heute für die Enten zuständig und bekommt den ersten Entenpunkt.");
                    }
                }
                break;
            }
            case DEBUG: {
                LOG.info("###### DEBUG ######");
                LOG.info("# duck father: " + dao.getDuckFather());
                LOG.info("# " + Util.toStatsString(dao.getDuckStats()));
                LOG.info("# pool temperature: " + dao.getTemperature());
                LOG.info("# todo: " + Util.formatList(dao.getTodoList()));
                LOG.info("# shopping: " + Util.formatList(dao.getShoppingList()));
                LOG.info("###### DEBUG ######");
                break;
            }
            case UNKNOWN: {
                sendMessage("Unbekannter Befehl. Tippe / für die Liste aller Befehle.");
                break;
            }
            default:
                LOG.warn("unhandled command case");
        }
    }

    @Override
    public String getBotToken() {
        return API_TOKEN;
    }


    private void sendMessage(String text) {
        LOG.info("Send message: " + text);
        SendMessage m = new SendMessage(CHAT_ID, text);
        try {
            sendMessage(m);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void sendStartupMessage() {
        sendMessage("Bin wieder online!");
    }

    private void sendPhoto(String filename) {
        SendPhoto message = new SendPhoto();
        message.setChatId(CHAT_ID);
        File f = new File(filename);
        message.setNewPhoto(f);
        try {
            sendPhoto(message);
        } catch (TelegramApiException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void sendRemindingMessage(int msgNumber) {
        switch (msgNumber) {
            case 1: {
                sendMessage(
                        "Die Enten müssen langsam ins Bett!\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
                break;
            }
            case 2: {
                sendMessage(
                        "Qick, Queck und Quack sind müde und würden gerne schlafen gehen!\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
                break;
            }
            case 3: {
                sendMessage(
                        "Die Enten müssen ins Bett!\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
                break;
            }
            case 4: {
                sendMessage(
                        "Haaaallllooo... Kümmert euch um die Enten!\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
                break;
            }
            case 5: {
                sendMessage(
                        "Alarm der Fuchs kommt und die Enten sind noch draußen!\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
                break;
            }
            case 6: {
                sendPhoto("fuchs.png");
                break;
            }
            default:
                sendMessage(
                        "Wer kümmert sich heute um die Enten?\nTippe /entenpapa oder /entenmama und sammle einen Entenpunkt.");
        }
    }

}
