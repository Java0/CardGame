package com.company.card;

public abstract class Card {

    char mark;

    int power;

    public Card(char mark, int power) {
        this.mark = mark;
        this.power = power;
    }

    public abstract char getMark() ;

    public abstract int getPower() ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (mark != card.mark) return false;
        return power == card.power;
    }

    @Override
    public int hashCode() {
        int result = mark;
        result = 31 * result + power;
        return result;
    }
}
