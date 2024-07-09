package org.ass03.part2B.model.remote;

import org.ass03.part2B.model.Cell;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Grid extends Remote {
    void setCellValue(int row, int col, int value) throws RemoteException;
    boolean isCompleted() throws RemoteException;
    void setCompleted() throws RemoteException;
    void printGrid() throws RemoteException;
    int getId() throws RemoteException;
    Cell[][] getGrid() throws RemoteException;
}
