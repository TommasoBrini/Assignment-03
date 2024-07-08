package org.ass03.part2A.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartView extends JFrame {
    private final JButton openGridViewButton;
    private final JButton newGameButton;

    public StartView(String title) {
        setTitle("Player-" + title + "  - Start View");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        openGridViewButton = new JButton("Join Game");
        openGridViewButton.setPreferredSize(new Dimension(800, 100));
        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(800, 100));
        centerPanel.add(openGridViewButton);
        centerPanel.add(newGameButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void addJoinGameListener(ActionListener listener) {
        openGridViewButton.addActionListener(listener);
    }

    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }
}