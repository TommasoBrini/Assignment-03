package org.ass03.part2B.model;

import java.awt.*;
import java.io.Serializable;

public class Cell implements Serializable {

    private int value;
    private final boolean initialSet;
    private Color color;

    public Cell(int value){
        this.value = value;
        this.initialSet = this.value != 0;
        this.color = Color.WHITE;
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

    public void setColor(Color color) {
        this.color = color;
    }
}
