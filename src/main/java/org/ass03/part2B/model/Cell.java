package org.ass03.part2B.model;

public class Cell {

    private int value;
    private final boolean initialSet;

    public Cell(int value){
        this.value = value;
        this.initialSet = this.value != 0;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public boolean isInitialSet(){
        return initialSet;
    }
}
