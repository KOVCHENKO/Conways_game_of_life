package com.company;

public class Main {

    public static void main(String[] args) {

        int n = 50;
        int m = 30;

        GameOfLife gameOfLife = new GameOfLife(n, m);
        gameOfLife.go();

    }
}
