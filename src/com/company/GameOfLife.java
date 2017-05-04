package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameOfLife {

    private int lifeSizeWidth;
    private int lifeSizeHeight;

    private String nameOfFrame = "Conway's game of life";
    private int frameStartLocation;
    private int pointRadius = 10;
    private int frameSizeWidth;
    private int frameSizeHeight;

    private int btnPanelSize = 100;

    boolean[][] lifeGeneration;
    boolean[][] nextGeneration;

    volatile boolean goNextGeneration = false;

    Canvas canvasPanel;

    public GameOfLife(int lifeSizeWidth, int lifeSizeHeight) {
        this.lifeSizeWidth = lifeSizeWidth;
        this.lifeSizeHeight = lifeSizeHeight;
        this.lifeGeneration = new boolean[lifeSizeWidth][lifeSizeHeight];
        this.frameSizeWidth = (lifeSizeWidth + 1) * pointRadius - 3;
        this.frameSizeHeight = (lifeSizeHeight + 1) * pointRadius - 3;
        this.frameStartLocation = 100;
        this.nextGeneration = new boolean[lifeSizeWidth][lifeSizeHeight];
    }

    public void go() {
        JFrame frame = new JFrame(nameOfFrame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameSizeWidth, frameSizeHeight + btnPanelSize);
        frame.setLocation(frameStartLocation, frameStartLocation);
        frame.setResizable(false);

        canvasPanel = new Canvas();
        canvasPanel.setBackground(Color.white);

        JButton fillButton = new JButton("Заполнить");
        fillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                for (int x = 0; x < lifeSizeWidth; x++) {
                    for (int y = 0; y < lifeSizeHeight; y++) {
                        lifeGeneration[x][y] = random.nextBoolean();
                    }
                }
                canvasPanel.repaint();
            }
        });

        JButton stepButton = new JButton("Шаг +");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processOfLife();
                canvasPanel.repaint();
            }
        });

        JButton goButton = new JButton("Пуск");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goNextGeneration = !goNextGeneration;
                goButton.setText(goNextGeneration? "Стоп" : "Пуск");
            }
        });



        JPanel btnPanel = new JPanel();
        btnPanel.add(fillButton);
        btnPanel.add(stepButton);
        btnPanel.add(goButton);

        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, btnPanel);

        frame.setVisible(true);

        while (true) {
            if (goNextGeneration) {
                processOfLife();
                canvasPanel.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }



    public void processOfLife() {
        for (int x = 0; x < lifeSizeWidth; x++) {
            for (int y = 0; y < lifeSizeHeight; y++) {
                int count = countNeighbors(x, y);
                nextGeneration[x][y] = lifeGeneration[x][y];
                nextGeneration[x][y] = (count == 3) ? true : nextGeneration[x][y];
                nextGeneration[x][y] = ((count < 2) || (count > 3)) ? false : nextGeneration[x][y];
            }
        }

        if (lifeSizeWidth > lifeSizeHeight) {
            for (int x = 0; x < lifeSizeWidth; x++) {
                System.arraycopy(nextGeneration[x], 0, lifeGeneration[x], 0, lifeSizeHeight);
            }
        } else {
            for (int x = 0; x < lifeSizeWidth; x++) {
                System.arraycopy(nextGeneration[x], 0, lifeGeneration[x], 0, lifeSizeWidth + (lifeSizeHeight - lifeSizeWidth));
            }
        }
    }

    public int countNeighbors(int x, int y) {
        int count = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int nX = x + dx;
                int nY = y + dy;
                nX = (nX < 0) ? lifeSizeWidth - 1 : nX;
                nY = (nY < 0) ? lifeSizeHeight - 1 : nY;
                nX = (nX > lifeSizeWidth - 1) ? 0 : nX;
                nY = (nY > lifeSizeHeight - 1) ? 0 : nY;
                count += (lifeGeneration[nX][nY]) ? 1 : 0;
            }
        }
        if (lifeGeneration[x][y]) { count--; }
        return count;
    }


    public class Canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < lifeSizeWidth; x++) {
                for (int y = 0; y < lifeSizeHeight; y++) {
                    if (lifeGeneration[x][y]) {
                        g.fillOval(x * pointRadius, y * pointRadius, pointRadius, pointRadius);
                    }
                }
            }
        }
    }

}
