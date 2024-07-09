package org.ass03.part2B.server;

import org.ass03.part2B.model.remote.GameManager;
import org.ass03.part2B.model.remote.GameManagerImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            GameManager gameManager = new GameManagerImpl();
            GameManager gameManagerStub = (GameManager) UnicastRemoteObject.exportObject(gameManager, 0);
            // Bind the remote object's stub in the registry
            Registry registry = null;

            registry = LocateRegistry.getRegistry();
            registry.rebind("GameManager", gameManagerStub);

            System.out.println("Game Manager ready to play!");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
