package org.ass03.part2B.model.remote;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserCallback extends Remote {
    void onGridCreated() throws RemoteException;
    void onGridUpdate(int gridId) throws RemoteException;
    void onCellSelected(int gridId, int row, int col, Color color) throws RemoteException;
    void onCellUnselected(int gridId, int row, int col) throws RemoteException;
    void onGridSubmitted(int gridId, String userId) throws RemoteException;
}
