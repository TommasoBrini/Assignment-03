package org.simulation.part2A.view;

import javax.swing.*;
import java.awt.*;

public class StartView extends JFrame {
    private final JButton joinGame;
    private final JButton createGame;

    public StartView() {
        super("Sudoku");

        this.setPreferredSize(new Dimension(720, 720));

        this.joinGame = new JButton();
        this.createGame = new JButton();

        this.createGame.setText("Create Game");
        this.joinGame.setText("Join Game");

        this.createGame.setPreferredSize(new Dimension(100, 50));
        this.joinGame.setPreferredSize(new Dimension(100, 50));

        JLabel title = new JLabel("Welcome to Sudoku!");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(title);
        panel.add(this.joinGame);
        panel.add(this.createGame);

        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    public JButton getJoinGameButton() {
        return this.joinGame;
    }

    public JButton getCreateGameButton() {
        return this.createGame;
    }

}
