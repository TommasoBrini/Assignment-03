package org.ass03.part2B.model.remote;

import org.ass03.part2B.model.User;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserCallbackImpl extends UnicastRemoteObject implements UserCallback {

    private final User user;

    public UserCallbackImpl(User user) throws RemoteException {
        this.user = user;
    }

    @Override
    public void onGridCreated() {
        user.notifyGridCreated();
    }

    @Override
    public void onGridUpdate(int gridId) throws RemoteException {
        user.notifyGridUpdated(gridId);
    }

    @Override
    public void onCellSelected(int gridId, int row, int col, Color color) throws RemoteException {
        user.notifyCellSelected(gridId, row, col, color);
    }

    @Override
    public void onCellUnselected(int gridId, int row, int col) throws RemoteException {
        user.notifyCellUnselect(gridId, row, col);
    }

    @Override
    public void onGridSubmitted(int gridId, String userId) throws RemoteException {
        user.notifyGridCompleted(gridId, userId);
    }


}
