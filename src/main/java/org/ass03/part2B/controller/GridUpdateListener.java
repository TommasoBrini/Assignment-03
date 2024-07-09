package org.ass03.part2B.controller;

import java.awt.*;
import java.rmi.RemoteException;

public interface GridUpdateListener {
    void onGridCreated() throws RemoteException;
    void onGridUpdated(int gridIndex) throws RemoteException;
    void onCellSelected(int gridId, int row, int col, Color color, String idUser);
    void onCellUnselected(int gridId, int row, int col);
    void onGridCompleted(int gridId, String userId) throws RemoteException;
}