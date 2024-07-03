package org.simulation.part2A.model;

import java.util.Optional;

public class Cell {

    private int value;
    private Optional<String> idUser;

    public Cell(int value){
        this.value = value;
        this.idUser = Optional.empty();
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public Optional<String> getIdUser(){
        return idUser;
    }

    public void setIdUser(String idUser){
        this.idUser = Optional.of(idUser);
    }
}
