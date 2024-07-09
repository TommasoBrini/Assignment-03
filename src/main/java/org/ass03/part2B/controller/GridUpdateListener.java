package org.ass03.part2B.controller;

import java.awt.*;
import java.rmi.RemoteException;

public interface GridUpdateListener {
    void onGridCreated();
    void onGridUpdated(int gridIndex);
    void onCellSelected(int gridId, int row, int col, Color color);
    void onCellUnselected(int gridId, int row, int col);
    void onGridCompleted(int gridId, String userId);
}