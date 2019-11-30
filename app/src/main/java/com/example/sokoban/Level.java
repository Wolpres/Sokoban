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

    public String getName() {
        return name;
    }
}
