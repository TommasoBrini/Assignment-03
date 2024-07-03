package org.simulation.part2A.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String id;
    private List<Grid> allGrids;

    public User(String id) {
        this.id = id;
        this.allGrids = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Grid> getAllGrids() {
        return allGrids;
    }

    public void createGrid() {
        Grid grid = new Grid(allGrids.size() + 1);
        allGrids.add(grid);
    }

    public Grid getGrid(int index){
        return allGrids.get(index);
    }

}
