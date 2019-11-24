package com.example.sokoban;

import java.io.Serializable;

public class Level implements Serializable {
    private int[][] originalMap = {
            {1, 1, 1, 1, 1},
            {1, 2, 3, 0, 1},
            {1, 4, 0, 6, 1},
            {1, 1, 1, 1, 1}
    };

    private int[][] actualMap;

    public Level() {
        actualMap = originalMap;
    }

    public int[][] getOriginalMap() {
        return originalMap;
    }

    public int[][] getActualMap() {
        return actualMap;
    }
}
