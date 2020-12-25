package com.company.card;

public class Poker extends Card {

    private char color;

    public Poker(char mark, int power) {
        super(mark, power);
    }

    public Poker(char mark, char color, int power) {
        super(mark, power);
        this.color = color;
    }

    @Override
    public char getMark() {
        return super.mark;
    }

    @Override
    public int getPower() {
        return power;
    }

    public char getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "" + mark;
    }


}


