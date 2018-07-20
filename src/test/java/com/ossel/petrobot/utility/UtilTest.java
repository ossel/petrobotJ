package com.ossel.petrobot.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Test
    public void testIsToday() throws ParseException {
        Date lastClaimTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
                .parse("2018-39-20 11:39:10,Dany Bro".split(",")[0]);
        Assert.assertTrue(Util.isToday(lastClaimTime));
    }


}
