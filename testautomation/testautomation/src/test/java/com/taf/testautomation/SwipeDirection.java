package com.taf.testautomation;

public enum SwipeDirection {
    RIGHT("right"),
    LEFT("left");

    private String direction;

    SwipeDirection(String direction) {
        this.direction = direction;
    }

    public String direction() {
        return direction;
    }

}
