package org.simulation.part2A.view;

import org.simulation.part2A.model.Cell;
import org.simulation.part2A.model.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameDetailsView extends JFrame {
    private final JPanel gamePanel;
    private final JButton backButton;

    public GameDetailsView() {
        setTitle("Sudoku Grid Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9, 9));
        add(gamePanel, BorderLayout.CENTER);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void displayGrid(Grid grid){
        gamePanel.removeAll();
        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JButton cellButton = new JButton(String.valueOf(cells[row][col].getValue()));
                cellButton.setEnabled(false); // Disable editing of buttons
                gamePanel.add(cellButton);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }
}
