package com.ossel.petrobot.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.ossel.petrobot.data.Item;

public class DaoTest {

    private Dao dao;

    @BeforeClass
    public static void deleteData() {
        File f = new File(Dao.DUCK_FATHER_LIST);
        f.deleteOnExit();
        File f2 = new File(Dao.SHOPPING_LIST);
        f2.deleteOnExit();
        File f3 = new File(Dao.TODO_LIST);
        f3.deleteOnExit();
    }

    @Before
    public void beforTest() {
        dao = Dao.getInstance();
        write(Dao.DUCK_FATHER_LIST, "2018-11-02,Dany\n2018-11-03,Luc");
        write(Dao.SHOPPING_LIST, "2018-06-01 12:00:00,ice,Dany\n2018-06-02 13:00:00,fish,Luc");
        write(Dao.TODO_LIST, "2018-06-01 12:00:00,test,Dany\n2018-06-02 13:00:00,test more,Luc");
    }

    @Test
    public void testDuckFatherList() {
        Map<String, Integer> stats = null;
        stats = dao.setDuckFather("Dany");
        stats = dao.setDuckFather("Luc");
        stats = dao.setDuckFather("Dany");
        Assert.assertEquals(Integer.valueOf(3), stats.get("Dany"));
        Assert.assertEquals(Integer.valueOf(2), stats.get("Luc"));
    }

    @Test
    public void testShoppingList() {
        dao.addShoppingItem(new Item("bread", "Dany"));
        Assert.assertEquals(3, dao.getShoppingList().size());
        Assert.assertEquals("fish", dao.getShoppingList().get(1).getText());
    }

    @Test
    public void testDeleteShoppingList() {
        dao.deleteShoppingList();
        Assert.assertEquals(0, dao.getShoppingList().size());
    }

    @Test
    public void testDeleteTodoItem() {
        Assert.assertEquals(2, dao.getTodoList().size());
        dao.addTodoItem(new Item("test even more", "Dany"));
        Assert.assertTrue(dao.deleteTodoItem(3)); // delete third element
        Assert.assertEquals(2, dao.getTodoList().size());
        Assert.assertFalse(dao.deleteTodoItem(3));
    }

    private void write(String filename, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
