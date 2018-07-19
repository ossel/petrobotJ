package com.ossel.petrobot;

import java.util.Calendar;
import java.util.Map;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.ossel.petrobot.data.Item;
import com.ossel.petrobot.data.Request;
import com.ossel.petrobot.services.Dao;
import com.ossel.petrobot.utility.Util;

public class PetroBot extends TelegramLongPollingBot {

    public static final Logger LOG = Logger.getLogger(PetroBot.class);

    private String CHAT_ID;
    private String API_TOKEN;

    Dao dao = Dao.getInstance();

    public PetroBot(String token, String chatId) {
        CHAT_ID = chatId;
        API_TOKEN = token;
    }

    public String getBotUsername() {
        return "PetroBot";
    }

    public void onUpdateReceived(Update data) {
        System.out.println("Update: " + data.toString());
        Request request = Util.getCommandFromMessage(data.getMessage().getText());
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
                sendMessage("Die Pooltemperatur beträgt 20 Grad Celsius.");
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
            case DELETE_TODO_LIST: {
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
                                + " ist heute für die Enten zuständig und bekommt seinen ersten Entenpunkt.");
                    }
                }
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

    public void sendDuckQuestion() {
        String question =
                "Wer kümmert sich heute um die Enten? Tippe /entenpapa oder /entenmama um die Verantwortung zu übernehmen.";
        LOG.info(question);
        sendMessage(question);
    }
}
