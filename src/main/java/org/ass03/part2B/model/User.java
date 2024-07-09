package org.ass03.part2B.model;

import org.ass03.part2B.controller.GridUpdateListener;
import org.ass03.part2B.model.remote.Grid;
import org.ass03.part2B.server.Server;
import org.ass03.part2B.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class User {
    private final String id;
    private final List<Grid> allGrids;
    private final List<GridUpdateListener> listeners;
    private final String color;
    private final Server server;

    public User(String id, String color) throws IOException, TimeoutException {
        this.id = id;
        this.color = color;
        this.allGrids = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.server = new Server();
    }

    public String getColor() {
        return color;
    }

    void setupConnection() throws IOException, TimeoutException {

    }

    public void addGridUpdateListener(GridUpdateListener listener){
        listeners.add(listener);
    }

    private void notifyGridCreated() throws RemoteException {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCreated();
        }
    }

    private void notifyGridUpdated(int gridId) throws RemoteException {
        for (GridUpdateListener listener : listeners) {
            listener.onGridUpdated(gridId);
        }
    }

    private void notifyCellSelected(int gridId, int row, int col, Color color, String idUser) {
        for (GridUpdateListener listener : listeners) {
            listener.onCellSelected(gridId, row, col, color, idUser);
        }
    }

    private void notifyCellUnselect(int gridId, int row, int col){
        for (GridUpdateListener listener : listeners) {
            listener.onCellUnselected(gridId, row, col);
        }
    }

    private void notifyGridCompleted(int gridId, String userId) throws RemoteException {
        for (GridUpdateListener listener : listeners) {
            listener.onGridCompleted(gridId, userId);
        }
    }

    public String getId() {
        return id;
    }

    public List<Grid> getAllGrids() {
        return allGrids;
    }

    public void createGrid() throws IOException, TimeoutException {
        int gridId = allGrids.size() + 1;
        Grid grid = new GridImpl(gridId);
        allGrids.add( grid);

        publishGrid(grid);
        notifyGridCreated();
    }

    public void publishGrid(Grid grid) throws IOException {
        this.server.registryGrid(grid);
    }

    public void updateGrid(int gridId, int row, int col, int value) throws IOException {

    }

    public void selectCell(int gridId, int row, int col) throws IOException {

    }

    public void unselectCell(int gridId, int row, int col) throws IOException {

    }

    public void submitGrid(int gridId) throws IOException {

    }

    public Grid getGrid(int index){
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            Grid grid = (Grid) registry.lookup("grid-" + 1);

            String response = Utils.toString(grid.getGrid());
            System.out.println("response: " + response);

            System.out.println("done.");
            return grid;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }

}
