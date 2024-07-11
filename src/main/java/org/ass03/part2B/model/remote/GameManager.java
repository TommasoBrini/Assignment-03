package org.ass03.part2B.model.remote;

import org.ass03.part2B.model.Grid;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.List;
import java.rmi.Remote;

public interface GameManager extends Remote {

    void registerCallback(UserCallback userCallback) throws RemoteException;

    void createGrid() throws RemoteException;

    void updateGrid(int gridId, int row, int col, int value) throws RemoteException;

    void selectCell(int gridId, int row, int col, Color color) throws RemoteException;

    void unselectCell(int gridId, int row, int col) throws RemoteException;

    void submitGrid(int gridId, String idUser) throws RemoteException;

    List<Grid> getAllGrids() throws RemoteException;

    Grid getGrid(int gridId) throws RemoteException;
}
