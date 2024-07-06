package org.ass03.part2A.view;

import org.ass03.part2A.model.Cell;
import org.ass03.part2A.model.Grid;
import org.ass03.part2A.model.User;
import org.ass03.part2A.utils.Utils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.Optional;

public class GameDetailsView extends JFrame {
    private final JPanel gamePanel;
    private final JButton backButton;
    private final JTextField[][] cellTextFields;
    private final JButton submitButton;

    public GameDetailsView(String title) {
        setTitle("Player-" + title + " - Sudoku Grid Details");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9, 9));
        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton("Submit Simulation"); // Inizializza il pulsante
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        cellTextFields = new JTextField[9][9]; // Initialize the JTextField array
    }


    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addSubmitButtonListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void displayGrid(Grid grid, User user) {
        gamePanel.removeAll();
        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final int currentRow = row; // final variables for inner class
                final int currentCol = col; // final variables for inner class
                JTextField cellTextField = new JTextField(cells[currentRow][currentCol].getValue() == 0 ? "" : String.valueOf(cells[currentRow][currentCol].getValue()));
                cellTextField.setHorizontalAlignment(JTextField.CENTER); // Align text to the center
                cellTextField.setPreferredSize(new Dimension(50, 50));

                // Set border for 3x3 grid
                int top = (currentRow % 3 == 0) ? 4 : 1;
                int left = (currentCol % 3 == 0) ? 4 : 1;
                int bottom = (currentRow == 8) ? 4 : 0;
                int right = (currentCol == 8) ? 4 : 0;
                cellTextField.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                if(cells[currentRow][currentCol].isInitialSet()) {
                    cellTextField.setBackground(Color.LIGHT_GRAY);
                    cellTextField.setEditable(false);
                    cellTextField.setFocusable(false);
                } else {
                    cellTextField.setEditable(true);

                    cellTextField.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });

                    cellTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            cells[currentRow][currentCol].setIdUser(Optional.of(user.getId()));
                            cellTextField.setBackground(Utils.getColorByName(user.getColor()));
                            try {
                                user.selectCell(grid.getId(), currentRow, currentCol);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            cellTextField.setBackground(Color.WHITE);
                            cells[currentRow][currentCol].setIdUser(Optional.empty());
                            try {
                                updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                                user.unselectCell(grid.getId(), currentRow, currentCol);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
                gamePanel.add(cellTextField);
                cellTextFields[row][col] = cellTextField;
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void updateCellValue(Grid grid, int row, int col, JTextField cellTextField, User user) throws IOException {
        try {
            if(cellTextField.getText().isEmpty()){
                grid.setCellValue(row, col, 0);
                user.updateGrid(grid.getId(), row, col, Integer.parseInt(cellTextField.getText()));
                return;
            }
            int newValue = Integer.parseInt(cellTextField.getText());
            if (newValue >= 1 && newValue <= 9) {
                grid.setCellValue(row, col, newValue); // Assuming a method like this exists
                user.updateGrid(grid.getId(), row, col, Integer.parseInt(cellTextField.getText()));
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        } catch (NumberFormatException e) {
            if (!cellTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        }
    }

    public void updateGrid(Grid grid) {
        grid.printGrid();
        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cellTextField = cellTextFields[row][col];
                if (cellTextField != null) {
                    int cellValue = cells[row][col].getValue();
                    cellTextField.setText(cellValue == 0 ? "" : String.valueOf(cellValue));
                }
            }
        }
    }

    public void colorCell(int row, int col, Color color){
        cellTextFields[row][col].setBackground(color);
        cellTextFields[row][col].setEditable(false);
    }

    public void uncolorCell(int row, int col){
        cellTextFields[row][col].setBackground(Color.WHITE);
        cellTextFields[row][col].setEditable(true);
    }

    public void displayMessage(String s) {
        System.out.println(s);
        JOptionPane.showMessageDialog(this, s);
    }
}
