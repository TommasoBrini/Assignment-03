package org.simulation.part2A.view;

import javax.swing.*;
import java.awt.*;

import org.simulation.part2A.model.Grid;

import java.awt.event.ActionListener;
import java.util.List;

public class GridView extends JFrame {

    private final JPanel gridPanel;
    private final JButton backButton;

    public GridView(String title) {
        setTitle("Player-" + title + " - Sudoku Grids");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Align left with horizontal and vertical gaps
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void displayGrids(List<Grid> grids, ActionListener listener) {
        gridPanel.removeAll();
        for (int i = 0; i < grids.size(); i++) {
            JButton gridButton = new JButton("Grid " + (i + 1));
            gridButton.setPreferredSize(new Dimension(200, 100)); // Fixed size for each button
            gridButton.addActionListener(listener);

            gridPanel.add(gridButton);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

}

