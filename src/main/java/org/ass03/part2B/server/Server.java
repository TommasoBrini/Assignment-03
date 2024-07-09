package org.ass03.part2B.server;

import org.ass03.part2B.model.GridImpl;
import org.ass03.part2B.model.remote.Grid;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {
    public Server() {}
    public void registryGrid(Grid grid) {
        try {
            Grid gridStub = (Grid) UnicastRemoteObject.exportObject(grid, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind("grid-" + grid.getId(), gridStub);

            System.out.println("Objects registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
