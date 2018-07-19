package com.ossel.petrobot.data;

import com.ossel.petrobot.data.enums.BotCommand;

public class Request {

    private BotCommand command;

    private String item;

    public Request(BotCommand command, String item) {
        super();
        this.command = command;
        this.item = item;
    }

    public BotCommand getCommand() {
        return command;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "Request [command=" + command + ", item=" + item + "]";
    }



}
