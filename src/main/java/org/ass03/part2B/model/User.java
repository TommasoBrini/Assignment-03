package org.ass03.part2B.model;

import org.ass03.part2B.controller.GridUpdateListener;
import org.ass03.part2B.model.remote.GameManager;
import org.ass03.part2B.model.remote.UserCallback;
import org.ass03.part2B.model.remote.UserCallbackImpl;
import org.ass03.part2B.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class User {
    private final String id;
    private final String color;
    private final GameManager gameManager;
    private UserCallback userCallback;
    private final List<GridUpdateListener> listeners;

    public User(String id, String color) throws RemoteException, NotBoundException {
        this.id = id;
        this.color = color;
        this.listeners = new ArrayList<>();

        Registry registry = LocateRegistry.getRegistry();
        gameManager = (GameManager) registry.lookup("GameManager");

        this.userCallback = new UserCallbackImpl(this);
        gameManager.registerCallback(userCallback);
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void addGridUpdateListener(GridUpdateListener listener){
        listeners.add(listener);
    }

    public void createGrid() throws IOException, TimeoutException {
        gameManager.createGrid();
        notifyGridCreated();
    }

    public void notifyGridCreated(){
        for (GridUpdateListener listener : listeners) {
            listener.onGridCreated();
        }
    }

    public void updateGrid(int gridId, int row, int col, int value) throws IOException {
        gameManager.updateGrid(gridId, row, col, value);
        notifyGridUpdated(gridId);
    }

    private void notifyGridUpdated(int gridId) throws RemoteException {
        for (GridUpdateListener listener : listeners) {
            listener.onGridUpdated(gridId);
        }
    }

    public void selectCell(int gridId, int row, int col) throws IOException {
        gameManager.selectCell(gridId, row, col, Utils.getColorByName(color));
        notifyCellSelected(gridId, row, col, Utils.getColorByName(color));
    }

    private void notifyCellSelected(int gridId, int row, int col, Color color) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellSelected(gridId, row, col, color);
        }
    }

    public void unselectCell(int gridId, int row, int col) throws IOException {
        gameManager.unselectCell(gridId, row, col);
        notifyCellUnselect(gridId, row, col);
    }

    private void notifyCellUnselect(int gridId, int row, int col){
        for (GridUpdateListener listener : listeners) {
            listener.onCellUnselected(gridId, row, col);
        }
    }

    public void submitGrid(int gridId) throws IOException {
        gameManager.submitGrid(gridId, id);
        notifyGridCompleted(gridId, id);
    }

    private void notifyGridCompleted(int gridId, String userId) throws RemoteException {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCompleted(gridId, userId);
        }
    }

    public List<Grid> getAllGrids() {
        try {
            return gameManager.getAllGrids();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Grid getGrid(int index) {
        try {
            return gameManager.getGrid(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
