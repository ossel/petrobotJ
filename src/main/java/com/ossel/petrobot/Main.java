package com.ossel.petrobot;

import java.util.Calendar;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.ossel.petrobot.services.Dao;
import com.ossel.petrobot.services.HttpServer;

public class Main {

    public static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            String errorMessage = "No Telegram API Token (args[0]) or Chat ID (args[1]) provided.";
            LOG.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        String token = args[0];
        String chatId = args[1];
        LOG.info("API TOKEN = " + token);
        LOG.info("CHAT ID = " + chatId);
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            final PetroBot petroBot = new PetroBot(token, chatId);
            petroBot.sendStartupMessage();
            telegramBotsApi.registerBot(petroBot);
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000 * 60 * 15); // every 15 minutes
                            Calendar cal = Calendar.getInstance();
                            int every = 45; // minutes
                            if (cal.get(Calendar.HOUR_OF_DAY) >= 18
                                    && Dao.getInstance().getDuckFather() == null) {
                                petroBot.sendMessage(
                                        "Wer kümmert sich heute um die Enten?\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendMessage(
                                        "Die Enten müssen langsam ins Bett!\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendMessage(
                                        "Qick, Queck und Quack sind müde und würden gerne schlafen gehen!\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendMessage(
                                        "Die Enten müssen ins Bett!\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendMessage(
                                        "Haaaallllooo... Kümmert euch um die Enten!\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendMessage(
                                        "Alarm der Fuchs kommt und die Enten sind noch draußen!\nTippe /entenpapa und sammle einen Entenpunkt.");
                                Thread.sleep(1000 * 60 * every);
                                petroBot.sendPhoto("fuchs.png");
                            }
                        } catch (InterruptedException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
            });
            t.start();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        LOG.info("PetroBot started...");
        SpringApplication.run(HttpServer.class, args);
    }

}
