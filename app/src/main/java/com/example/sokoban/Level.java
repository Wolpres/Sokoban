package com.example.sokoban;

import java.io.Serializable;

public class Level implements Serializable {
    private Integer[][] map;
    private String name;

    public Level(Integer[][] map, String name) {
        this.map = map;
        this.name = name;
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
}
