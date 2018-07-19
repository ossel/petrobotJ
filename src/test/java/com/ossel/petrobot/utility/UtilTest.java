package com.ossel.petrobot.utility;

import org.junit.Assert;
import org.junit.Test;
import com.ossel.petrobot.data.Request;
import com.ossel.petrobot.data.enums.BotCommand;

public class UtilTest {

    @Test
    public void testDeleteTodoItem() {
        Request request = Util.getRequestFromMessage("/todo_loeschen 1");
        Assert.assertEquals("1", request.getItem());
        Assert.assertEquals(BotCommand.DELETE_TODO_ITEM, request.getCommand());
    }


}
