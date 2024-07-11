package org.ass03.part2B.model.remote;

import org.ass03.part2B.model.Grid;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameManagerImpl implements GameManager{

    private final List<Grid> allGrids;
    private final List<UserCallback> callbacks;

    public GameManagerImpl() {
        this.allGrids = new ArrayList<>();
        this.callbacks = new ArrayList<>();
    }

    @Override
    public void registerCallback(UserCallback userCallback) {
        callbacks.add(userCallback);
    }

    @Override
    public synchronized void createGrid() {
        int gridId = allGrids.size();
        Grid grid = new Grid(gridId);
        allGrids.add(grid);
        for (UserCallback callback : callbacks) {
            try {
                callback.onGridCreated();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateGrid(int gridId, int row, int col, int value) {
        allGrids.get(gridId).setCellValue(row, col, value);
        for (UserCallback callback : callbacks) {
            try {
                callback.onGridUpdate(gridId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void selectCell(int gridId, int row, int col, Color color) {
        allGrids.get(gridId).setColor(row, col, color);
        for (UserCallback callback : callbacks) {
            try {
                callback.onCellSelected(gridId, row, col, color);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void unselectCell(int gridId, int row, int col) {
        allGrids.get(gridId).setColor(row, col, Color.WHITE);
        for (UserCallback callback : callbacks) {
            try {
                callback.onCellUnselected(gridId, row, col);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void submitGrid(int gridId, String idUser) {
        allGrids.get(gridId).setCompleted();
        for (UserCallback callback : callbacks) {
            try {
                callback.onGridSubmitted(gridId, idUser);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Grid> getAllGrids() {
        return allGrids;
    }

    @Override
    public Grid getGrid(int gridId) {
        return allGrids.get(gridId);
    }
}
