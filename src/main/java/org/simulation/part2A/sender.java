package org.simulation.part2A;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        Player p = new Player("1", "localhost");
        p.updateGrid(1, 1, 1);
    }
}
