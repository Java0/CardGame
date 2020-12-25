package com.company;

import com.company.card.Poker;
import com.company.card.Pokers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class CardGame {

    public static final int DOU_DI_ZHU = Pokers.SIMPLE;

    private boolean isEnd = false;


    public void run(int gameType) {

        //初始化牌组
        Pokers.addCardToPool(gameType);

        //玩家加入
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            players.add(new Player("张" + (char) ('三' + i)));
        }

        ArrayList<Poker> cardPool = Pokers.getPool();
        assert cardPool != null;

        //洗牌
        Collections.shuffle(cardPool);


        // 抢地主
        Scanner sc = new Scanner(System.in);

        int ownerIndex = 0;

        for (int i = new Random().nextInt(players.size()); i <= players.size(); i++) {

            if (i > players.size()-1) {
                i = i - players.size();
            }

            Player p = players.get(i);
            System.out.println("你是" + p.getName() + ",是否抢地主(抢/不抢)");
            String str = sc.next();
            if ("抢".equals(str)) {
                p.setOwner();
                ownerIndex = i;
                break;
            }else if(!"不抢".equals(str)){
                System.out.println("憋给我整其他玩意儿，抢还是不抢");
                i--;
            }

        }

        //发牌
        Pokers.dealCards(players, cardPool, players.get(ownerIndex));

        //看牌
        Pokers.displayCard(players);

        //轮流出牌
        int index = ownerIndex;
        while (!isEnd) {
            if (index > players.size()-1) {
                index = index - players.size();
            }
            Player currentPlayer = players.get(index);
            Pokers.playCard(currentPlayer);
            if (Pokers.canNext) {
                index++;
            }

            Pokers.displayCard(players);

            if (currentPlayer.getHands().isEmpty()) {
                isEnd = true;
                System.out.println(currentPlayer.getName() + "赢了");
            }

        }

    }
}
