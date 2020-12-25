package com.company;

import com.company.card.Poker;

import java.util.ArrayList;

public class Player{

    private String name;

    private ArrayList<Poker> hands = new ArrayList<>();

    public void setHands(ArrayList<Poker> hands) {
        this.hands = hands;
    }

    private boolean isOwner;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Poker> getHands() {
        return hands;
    }

    public void addHand(Poker p) {
        this.hands.add(p);
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner() {
        isOwner = true;
    }





}
