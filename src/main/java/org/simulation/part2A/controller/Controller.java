package org.simulation.part2A.controller;

import org.simulation.part2A.model.Grid;
import org.simulation.part2A.model.User;
import org.simulation.part2A.view.GridView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private User user;
    private GridView view;

    public Controller(User user, GridView view) {
        this.user = user;
        this.view = view;
        initView();
    }

    private void initView() {
        view.displayGrids(user.getAllGrids());
    }
}
