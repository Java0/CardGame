package com.company.card;

import com.company.Player;
import com.company.Rule;

import java.util.*;

public class Pokers {

    public static char[] simpleColors = {'♥', '♠', '♦', '♣'};

    public static char[] simpleSymbols = {'3', '4', '5', '6', '7', '8', '9', 'I', 'J', 'Q', 'K', 'A', '2'};

    public static char[] jokers = {'鬼', '王'};

    public static final int SIMPLE = 0;
    public static final int WITH_COLOR = 1;
    public static final int NO_JOKER = 2;

    private static ArrayList<Poker> pool = new ArrayList<>();

    public static boolean canNext = false;

    public static void addCardToPool(int poolType) {
        switch (poolType) {

            case SIMPLE:
                addCards(4, simpleSymbols.length, simpleSymbols, false, null);
                addJoker();
                break;

            case WITH_COLOR:
                addCards(simpleColors.length, simpleSymbols.length, simpleSymbols, true, simpleColors);
                addJoker();
                break;
        }
    }

    public static ArrayList<Poker> getPool() {
        if (pool.size() != 0) {
            return pool;
        } else {
            return null;
        }
    }


    private static void addCards(int repeatTimes, int symbolNum, char[] symbols, boolean withColor, char[] colors) {
        if (withColor) {
            for (int i = 0; i < repeatTimes; i++) {
                for (int j = 0; j < symbolNum; j++) {
                    pool.add(new Poker(symbols[j], colors[i], j));
                }
            }
        } else {
            for (int i = 0; i < repeatTimes; i++) {
                for (int j = 0; j < symbolNum; j++) {
                    pool.add(new Poker(symbols[j], j));
                }
            }
        }
    }

    private static void addJoker() {
        pool.add(new Poker('鬼', 13));
        pool.add(new Poker('王', 14));
    }

    public static void dealCards(ArrayList<Player> players, ArrayList<Poker> cardPool, Player owner) {
        //发牌
        for (int i = 0; i < cardPool.size() - 3; i++) {
            players.get(i % players.size()).addHand(cardPool.get(i));
        }
        for (int i = cardPool.size() - 1; i >= cardPool.size() - 3; i--) {
            owner.addHand(cardPool.get(i));
        }
    }

    public static void dealCards(ArrayList<Player> players, ArrayList<Poker> cardPool) {
        //发牌
        for (int i = 0; i < cardPool.size(); i++) {
            players.get(i % players.size()).addHand(cardPool.get(i));
        }
    }


    public static ArrayList<Poker> charToCard(char[] symbolsList, char[] inputChars) {

        ArrayList<Poker> tempList = new ArrayList<>();
        int timer = 0;

        for (char inputChar : inputChars) {
            for (int j = 0; j < symbolsList.length; j++) {
                if (inputChar == symbolsList[j]) {
                    timer++;
                    tempList.add(new Poker(inputChar, j));
                }
            }

            if (inputChar == '鬼') {
                timer++;
                tempList.add(new Poker(inputChar, 13));
            }

            if (inputChar == '王') {
                timer++;
                tempList.add(new Poker(inputChar, 14));
            }

        }

        if (timer == inputChars.length) {
            return tempList;
        } else {
            return null;
        }
    }

    public static void displayCard(ArrayList<Player> players) {
        for (Player p : players) {

            Collections.sort(p.getHands(), (p1, p2) -> p1.getPower() - p2.getPower());

            if (p.isOwner()) {
                System.out.print('\n' + p.getName() + "(地主):");
            } else {
                System.out.print('\n' + p.getName() + ":\t  ");
            }

            for (Poker poker : p.getHands()) {
                if (poker.getMark() != 'I') {
                    System.out.print(poker.getMark() + " ");
                } else {
                    System.out.print(10 + " ");
                }
            }
        }
    }

    private static Rule lastRule;

    private static int passCount;

    public static void playCard(Player you) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n\n你是" + you.getName() + ",请开始你的表演");

        String playerOut = sc.next();

        if (playerOut.contains("10")) {
            playerOut = playerOut.replace("10", "I");
        }

        playerOut = playerOut.toUpperCase();

        char[] outChars = playerOut.toCharArray();

        ArrayList<Poker> outPokers = Pokers.charToCard(Pokers.simpleSymbols, outChars);

        //出牌
        if ("PASS".equals(playerOut)) {
            passCount++;
            canNext = true;

            if (passCount == 2) {
                lastRule = null;
            }
        } else if (outPokers != null) {

            //将出的牌从小到大排序
            Collections.sort(outPokers, (p1, p2) -> p1.getPower() - p2.getPower());

            //如果出的牌和手里的牌相同，判断规则后出牌

            boolean havePokers = true;

            ArrayList<Poker> playerHands = (ArrayList<Poker>) you.getHands().clone();
            if (outPokers != null) {

                for (Poker outPoker : outPokers) {
                    if (!playerHands.remove(outPoker)) {
                        havePokers = false;
                        break;
                    }
                }

                if (havePokers) {
                    Rule currentRule = new Rule(outPokers);
                    //如果符合规则
                    if (currentRule.meetConditions(lastRule)) {
                        passCount = 0;

                        canNext = true;

                        lastRule = currentRule;

                        you.setHands(playerHands);


                        //显示已出的牌
                        for (Poker poker : outPokers) {
                            if (poker.getMark() != 'I') {
                                System.out.print(poker.getMark() + " ");
                            } else {
                                System.out.print(10 + " ");
                            }
                        }

                    } else {
                        System.out.println("\n请按规则出牌");
                        canNext = false;
                    }

                } else {
                    canNext = false;
                    System.out.println("\n您无此牌");
                }

            } else {
                System.out.println("你丫出的是牌吗?");
            }

        }
    }
}
