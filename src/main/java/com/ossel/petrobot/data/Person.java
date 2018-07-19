package com.ossel.petrobot.data;

public class Person {

    private String name;

    private Integer points;

    public Person(String name, Integer points) {
        super();
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

}
