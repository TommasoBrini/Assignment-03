package org.simulation.part2A;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException {
        if (args.length < 2) {
            System.err.println("Usage: Player <playerId> <host>");
            System.exit(1);
        }
        String playerId = args[0];
        String host = args[1];

        Player player = new Player(playerId, host);
        player.joinGrid();

    }
}
