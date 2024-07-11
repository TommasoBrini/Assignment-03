package org.ass03.part2B.view;

import org.ass03.part2B.model.Grid;

import javax.swing.*;
import java.awt.*;
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
            String name = "Grid " + (i);
            if (grids.get(i).isCompleted()) {
                name += " (Completed)";
            }
            JButton gridButton = new JButton(name);
            gridButton.setPreferredSize(new Dimension(200, 100)); // Fixed size for each button
            gridButton.addActionListener(listener);
            gridPanel.add(gridButton);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

}

