package org.simulation.part2A.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridView extends JFrame {
    private static final int NUM_CELL_FOR_GRID = 9;
    private static final int NUM_GRID = 9;

    private final JPanel gridPanel;
    private final JPanel centeredGrid;
    private final JButton backButton;
    //private

    public GridView() {
        super("Sudoku");

        this.backButton = new JButton("<");
        this.backButton.setPreferredSize(new Dimension(50, 50));
        this.backButton.setVerticalAlignment(JButton.TOP);

        gridPanel = new JPanel(new GridLayout(3,4));
        this.setGrid();
        centeredGrid = new JPanel(new GridBagLayout());
        centeredGrid.add(backButton);
        centeredGrid.add(gridPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(centeredGrid);
        this.pack();
        this.setLocationByPlatform(true);
    }

    private void setGrid() {
        for(int j = 0; j < NUM_GRID; j++) {
            JPanel smallerGridPanel = new JPanel(new GridLayout(3,3));
            for (int i = 0; i < NUM_CELL_FOR_GRID; i++) {
                final JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER); //Center text horizontally in the button.
                cell.setPreferredSize(new Dimension(80, 80)); //Set the size of the button.
                smallerGridPanel.add(cell);
            }
            smallerGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            this.gridPanel.add(smallerGridPanel);
        }
    }

    public void setGridValues(List<List<Integer>> values) {
        for (int i = 0; i < NUM_GRID; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    JTextField textField = (JTextField) this.gridPanel.getComponent(i).getComponentAt(j,k);
                    textField.setText(String.valueOf(values.get(j).get(k)));
                }
            }
        }
    }

    public JButton getBackButton() {
        return this.backButton;
    }
}
