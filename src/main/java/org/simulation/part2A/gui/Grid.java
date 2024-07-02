package org.simulation.part2A.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Grid extends JFrame {
    private static final int NUM_CELL_FOR_GRID = 9;
    private static final int NUM_GRID = 9;

    private final JPanel grid;
    private final JPanel centeredGrid;

    public Grid() {
        super("Sudoku");

        grid = new JPanel(new GridLayout(3,3));
        this.fillGrid();
        centeredGrid = new JPanel(new GridBagLayout());
        centeredGrid.add(grid);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(centeredGrid);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    private void fillGrid() {
        for(int j = 0; j < NUM_GRID; j++) {
            JPanel smallerGrid = new JPanel(new GridLayout(3,3));
            for (int i = 0; i < NUM_CELL_FOR_GRID; i++) {
                final JButton cell = new JButton();
                cell.setHorizontalAlignment(JButton.CENTER); //Center text horizontally in the button.
                cell.setPreferredSize(new Dimension(80, 80)); //Set the size of the button.
                smallerGrid.add(cell);
            }
            smallerGrid.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.grid.add(smallerGrid);
        }
    }
}
