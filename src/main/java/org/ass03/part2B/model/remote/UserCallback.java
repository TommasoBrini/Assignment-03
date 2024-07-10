package org.ass03.part2B.model.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserCallback extends Remote {
    void onGridCreated() throws RemoteException;
}
