package com.ossel.petrobot.utility;

import org.junit.Assert;
import org.junit.Test;
import com.ossel.petrobot.api.TelegramRequest;
import com.ossel.petrobot.data.enums.BotCommand;

public class TelegramRequestTest {

    @Test
    public void testPoolCommand() {
        TelegramRequest request = new TelegramRequest("/pool message", "Dany");
        Assert.assertEquals("message", request.getMessage());
        Assert.assertEquals(BotCommand.SHOW_POOL_TEMPERATURE, request.getCommand());
    }

    @Test
    public void testAddTodoItemCommand() {
        TelegramRequest request = new TelegramRequest("/todo the task I have to do", "Dany");
        Assert.assertEquals("the task I have to do", request.getMessage());
        Assert.assertNotNull(request.getItems());
        Assert.assertEquals(BotCommand.ADD_TODO_ITEM, request.getCommand());
    }

    @Test
    public void testDeleteTodoItem() {
        TelegramRequest request = new TelegramRequest("/todo_loeschen 1", "Dany");
        Assert.assertEquals("1", request.getMessage());
        Assert.assertEquals(BotCommand.DELETE_TODO_ITEM, request.getCommand());
    }

    @Test
    public void testShowTodoListCommand() {
        TelegramRequest request = new TelegramRequest("/todoliste", "Dany");
        Assert.assertEquals(BotCommand.SHOW_TODO_LIST, request.getCommand());
        request = new TelegramRequest("/todolist", "Dany");
        Assert.assertEquals(BotCommand.SHOW_TODO_LIST, request.getCommand());
    }

    @Test
    public void testAddShoppingItemCommand() {
        TelegramRequest request = new TelegramRequest("/einkauf fish", "Dany");
        Assert.assertEquals("fish", request.getMessage());
        Assert.assertNotNull(request.getItems());
        Assert.assertEquals(BotCommand.ADD_SHOPPING_ITEM, request.getCommand());
    }

    @Test
    public void testDeleteShoppingListCommand() {
        TelegramRequest request = new TelegramRequest("/einkaufsliste_loeschen", "Dany");
        Assert.assertEquals(BotCommand.DELETE_SHOPPING_LIST, request.getCommand());
    }

    @Test
    public void testShowShoppingListCommand() {
        TelegramRequest request = new TelegramRequest("/einkaufliste", "Dany");
        Assert.assertEquals(BotCommand.SHOW_SHOPPING_LIST, request.getCommand());
        request = new TelegramRequest("/einkaufsliste", "Dany");
        Assert.assertEquals(BotCommand.SHOW_SHOPPING_LIST, request.getCommand());
    }

    @Test
    public void testClaimDuckFatherCommand() {
        TelegramRequest request = new TelegramRequest("/entenpapa", "Dany");
        Assert.assertEquals(BotCommand.CLAIM_DUCK_RESPONSIBILITY, request.getCommand());
        request = new TelegramRequest("/entenmama", "Dany");
        Assert.assertEquals(BotCommand.CLAIM_DUCK_RESPONSIBILITY, request.getCommand());
    }

    @Test
    public void testShowDuckStatsCommand() {
        TelegramRequest request = new TelegramRequest("/entenpunkte", "Dany");
        Assert.assertEquals(BotCommand.SHOW_DUCK_STATS, request.getCommand());
    }

    @Test
    public void testShowDuckFatherCommand() {
        TelegramRequest request = new TelegramRequest("/entendienst", "Dany");
        Assert.assertEquals(BotCommand.SHOW_DUCK_FATHER, request.getCommand());
    }

    @Test
    public void testDebugCommand() {
        TelegramRequest request = new TelegramRequest("/debug", "Dany");
        Assert.assertEquals(BotCommand.DEBUG, request.getCommand());
    }

    @Test
    public void testPollCommand() {
        TelegramRequest request = new TelegramRequest("/umfrage", "Dany");
        Assert.assertEquals(BotCommand.POLL, request.getCommand());
    }

    @Test
    public void testPollFinishedCommand() {
        TelegramRequest request = new TelegramRequest("/umfrage_fertig", "Dany");
        Assert.assertEquals(BotCommand.POLL_FINISHED, request.getCommand());
    }

    @Test
    public void testVoteCommand() {
        TelegramRequest request = new TelegramRequest("/1.foo", "Dany");
        Assert.assertEquals(BotCommand.POLL_VOTE, request.getCommand());
        Assert.assertEquals("1", request.getMessage());
    }

    @Test
    public void testUnknownCommand() {
        TelegramRequest request = new TelegramRequest("/foo", "Dany");
        Assert.assertEquals(BotCommand.UNKNOWN, request.getCommand());
    }

    @Test
    public void testCommandTypo() {
        TelegramRequest request = new TelegramRequest("/EnteNdienst", "Dany");
        Assert.assertEquals(BotCommand.SHOW_DUCK_FATHER, request.getCommand());
    }

}
