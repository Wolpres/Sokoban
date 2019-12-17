package com.example.sokoban;

import java.io.Serializable;

public class Level implements Serializable {
    private int id;
    private Integer[][] map;
    private String name;
    private boolean done;

    public Level(Integer[][] map, String name) {
        this.map = map;
        this.name = name;
    }

    public Level(int id, String name, boolean done) {
        this.id = id;
        this.name = name;
        this.done = done;
    }

    public Integer[][] getMap() {
        return map;
    }

    public Integer[][] getMapClone() {
    	Integer[][] clone = new Integer[map.length][];
    	for (int i = 0 ; i < map.length ; i++)
    	    clone[i] = map[i].clone();
        return clone;
    }

    public String getName() {
        return name;
    }



    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getId() {
        return id;
    }
}
