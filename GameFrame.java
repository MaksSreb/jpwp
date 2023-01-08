package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameFrame extends JFrame implements ActionListener {


    GamePanel game;
    JButton resetButton;
    JButton exitButton;

    GameFrame(){

        this.setSize(816,632);
        this.setTitle("RapidEel");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);

        resetButton = new JButton();
        resetButton.setText("New Game");
        resetButton.setSize(180,80);
        resetButton.setLocation(310,10);
        resetButton.addActionListener(this);
        resetButton.setFont(new Font("Comic Sans",Font.BOLD,20));
        resetButton.setBackground(Color.lightGray);
        resetButton.setFocusable(false);

        exitButton = new JButton();
        exitButton.setText("Quit");
        exitButton.setSize(180,80);
        exitButton.setLocation(610,10);
        exitButton.addActionListener(this);
        exitButton.setFont(new Font("Comic Sans",Font.BOLD,20));
        exitButton.setBackground(Color.red);
        exitButton.setFocusable(false);

        game = new GamePanel();
        this.add(resetButton);
        this.add(exitButton);
        this.add(game);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==resetButton){
            this.remove(game);
            game = new GamePanel();
            this.add(game);
            game.requestFocusInWindow();
            SwingUtilities.updateComponentTreeUI(this);
        }
        if(e.getSource()==exitButton){
            this.remove(game);
            System.exit(0);
        }
    }
}
