package org.ass03.part2B.model.remote;

import org.ass03.part2B.model.User;

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
}
