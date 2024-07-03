package org.simulation.part2A.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import org.simulation.part2A.model.Cell;
import org.simulation.part2A.model.Grid;
import java.util.List;

public class GridView extends JFrame {

    private JPanel gridPanel;

    public GridView() {
        setTitle("Sudoku Grids");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Align left with horizontal and vertical gaps
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void displayGrids(List<Grid> grids) {
        gridPanel.removeAll();
        for (int i = 0; i < grids.size(); i++) {
            JButton gridButton = new JButton("Grid " + (i + 1));
            gridButton.setPreferredSize(new Dimension(200, 100)); // Fixed size for each button
            gridPanel.add(gridButton);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

}

