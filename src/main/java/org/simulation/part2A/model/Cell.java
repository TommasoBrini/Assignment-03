package org.simulation.part2A.model;

import java.util.Optional;

public class Cell {

    private int value;
    // TODO da gestire idUser
    private Optional<String> idUser;
    private final boolean initialSet;

    public Cell(int value){
        this.value = value;
        if (this.value != 0) {
            this.initialSet = true;
        } else {
            this.initialSet = false;
        }
        this.idUser = Optional.empty();
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

    public Optional<String> getIdUser(){
        return idUser;
    }

    public void setIdUser(String idUser){
        this.idUser = Optional.of(idUser);
    }
}
