package com.example.sokoban;

public enum MoveDirection {
    NONE(0),
    THERE(1),
    BACK(-1);

    public int n;
    MoveDirection(int n) {
        this.n = n;
    }
}
