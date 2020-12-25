package com.company;

import com.company.card.Poker;
import com.company.card.Pokers;

import java.util.*;

public class Rule {

    private String rule;
    private int power;


    //如果传入的牌经过判断符合规则，则为true
    private boolean canPlayCards;

    public String getRule() {
        return rule;
    }

    public int getPower() {
        return power;
    }


    public Rule(ArrayList<Poker> inputPokers) {
        ArrayList<Poker> oneTypePokerList = Pokers.getOneTypePokerList(inputPokers);
        Map<Poker, Integer> repeatPokerMap = Pokers.getRepeatPokerMap(inputPokers);
        switch (inputPokers.size()) {
            case 1:
                this.power = inputPokers.get(0).getPower();
                setRule("单牌");
                break;
            case 2:
                if (isAA(oneTypePokerList, repeatPokerMap)) {
                    setRule("对牌");
                } else if (isSuperBomb(oneTypePokerList)) {
                    setRule("王炸");
                }
                break;
            case 3:
                if (isAAA(oneTypePokerList, repeatPokerMap)) {
                    setRule("3带0");
                }
                break;
            case 4:
                if (isAAAB(oneTypePokerList, repeatPokerMap)) {
                    setRule("3带1");
                } else if (isBomb(oneTypePokerList, repeatPokerMap)) {
                    setRule("炸弹");
                }
                break;
            case 5:
                if (isAAABB(oneTypePokerList, repeatPokerMap)) {
                    setRule("3带2");
                } else if (isStraight(oneTypePokerList, repeatPokerMap)) {
                    setRule("顺子");
                }
                break;
            default:

                if (isStraight(oneTypePokerList, repeatPokerMap)) {
                    setRule("顺子");
                } else if (isDoubleStraight(oneTypePokerList, repeatPokerMap)) {
                    setRule("连对");
                }else if(isPlane(oneTypePokerList,repeatPokerMap)){
                    setRule("飞机");
                }

                break;
        }

    }

    private void setRule(String rule) {
        this.canPlayCards = true;
        this.rule = rule;
    }

    private boolean isAA(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> samePokerMap) {
        return isOneTypeRepeat(oneTypePokerList, samePokerMap, 2);
    }

    private boolean isAAA(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {
        return isOneTypeRepeat(oneTypePokerList, repeatPokerMap, 3);
    }

    private boolean isAAAB(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {
        if (oneTypePokerList.size() == 2) {
            for (Poker poker : oneTypePokerList) {
                if (repeatPokerMap.get(poker) == 3) {
                    this.power = poker.getPower() * 3;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAAABB(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {
        if (oneTypePokerList.size() == 2) {
            for (Poker poker : oneTypePokerList) {
                if (repeatPokerMap.get(poker) == 3) {
                    this.power = poker.getPower() * 3;
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isBomb(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {
        return isOneTypeRepeat(oneTypePokerList, repeatPokerMap, 4);
    }

    private boolean isSuperBomb(ArrayList<Poker> oneTypePokerList) {
        return oneTypePokerList.size() == 2 && oneTypePokerList.get(0).getMark() == '鬼' && oneTypePokerList.get(1).getMark() == '王';
    }

    private int straightLength;

    public int getStraightLength() {
        return straightLength;
    }

    private boolean isStraight(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {

        if (oneTypePokerList.size() >= 5 && notContainsUselessPoker(oneTypePokerList) && repeatPokerMap.get(oneTypePokerList.get(0)) == 1) {

            Poker temp = oneTypePokerList.get(0);

            this.power = temp.getPower();

            for (int i = 1; i < oneTypePokerList.size(); i++) {
                if (repeatPokerMap.get(oneTypePokerList.get(i)) == 1 && Pokers.isSerialNumber(temp, oneTypePokerList.get(i))) {
                    temp = oneTypePokerList.get(i);
                    this.power = this.power + oneTypePokerList.get(i).getPower();
                } else {
                    this.power = 0;
                    return false;
                }
            }

        } else {
            return false;
        }

        this.straightLength = oneTypePokerList.size();

        return true;
    }

    private boolean isDoubleStraight(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {

        if (oneTypePokerList.size() >= 3 && notContainsUselessPoker(oneTypePokerList) && repeatPokerMap.get(oneTypePokerList.get(0)) == 2) {

            Poker temp = oneTypePokerList.get(0);

            this.power = temp.getPower() * 2;

            for (int i = 1; i < oneTypePokerList.size(); i++) {

                if (repeatPokerMap.get(oneTypePokerList.get(i)) == 2 && Pokers.isSerialNumber(temp, oneTypePokerList.get(i))) {
                    temp = oneTypePokerList.get(i);
                    this.power = this.power + (oneTypePokerList.get(i).getPower() * 2);
                } else {
                    this.power = 0;
                    return false;
                }

            }

        } else {
            return false;
        }

        this.straightLength = oneTypePokerList.size();

        return true;
    }

    private boolean isPlane(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap) {

        int size = oneTypePokerList.size();

        ArrayList<Poker> ThreeCountList = new ArrayList<>();
        ArrayList<Poker> xCountList = new ArrayList<>();


        if (size >= 3) {

            if (size == 3) {
                for (Poker poker : oneTypePokerList) {
                    if (repeatPokerMap.get(poker) == 3) {
                        ThreeCountList.add(poker);
                    } else {
                        xCountList.add(poker);
                    }
                }

                if (ThreeCountList.size() >= 2) {

                    if (ThreeCountList.size() == 3) {

                        Poker temp = ThreeCountList.get(0);
                        this.power = temp.getPower() * 3;

                        for (int i = 1; i < ThreeCountList.size(); i++) {
                            if (Pokers.isSerialNumber(temp, ThreeCountList.get(i))) {
                                temp = ThreeCountList.get(i);
                                this.power = this.power + (ThreeCountList.get(i).getPower() * 3);
                                this.straightLength++;
                            } else {
                                this.straightLength = 0;
                                return false;
                            }
                        }
                    } else {
                        Poker temp = ThreeCountList.get(0);
                        this.power = temp.getPower() * 3;

                        for (int i = 1; i < ThreeCountList.size(); i++) {
                            if (Pokers.isSerialNumber(temp, ThreeCountList.get(i)) && (repeatPokerMap.get(xCountList.get(0)) == 2 || repeatPokerMap.get(xCountList.get(0)) == 4)) {
                                temp = ThreeCountList.get(i);
                                this.power = this.power + (ThreeCountList.get(i).getPower() * 3);
                                this.straightLength++;
                            } else {
                                this.straightLength = 0;
                                return false;
                            }
                        }
                    }


                }

            } else {

                for (Poker poker : oneTypePokerList) {
                    if (repeatPokerMap.get(poker) == 3) {
                        ThreeCountList.add(poker);
                    } else {
                        xCountList.add(poker);
                    }
                }

                if (xCountList.size() != 0 && ThreeCountList.size()==xCountList.size()) {

                    Poker temp = ThreeCountList.get(0);
                    this.power = temp.getPower() * 3;

                    for (int i = 1; i < ThreeCountList.size(); i++) {
                        if (Pokers.isSerialNumber(temp, ThreeCountList.get(i))) {
                            temp = ThreeCountList.get(i);
                            this.power = this.power + (ThreeCountList.get(i).getPower() * 3);
                            this.straightLength++;
                        } else {
                            this.straightLength = 0;
                            return false;
                        }
                    }

                    int count = repeatPokerMap.get(xCountList.get(0));
                    for (int i = 1; i < xCountList.size(); i++) {
                        Poker poker = xCountList.get(i);
                        if(repeatPokerMap.get(poker) == count){
                            count = repeatPokerMap.get(poker);
                        }else {
                            return false;
                        }
                    }

                }else if(xCountList.size() == 0){

                    Poker temp = ThreeCountList.get(0);
                    this.power = temp.getPower() * 3;

                    for (int i = 1; i < ThreeCountList.size(); i++) {
                        if (Pokers.isSerialNumber(temp, ThreeCountList.get(i))) {
                            temp = ThreeCountList.get(i);
                            this.power = this.power + (ThreeCountList.get(i).getPower() * 3);
                            this.straightLength++;
                        } else {
                            this.straightLength = 0;
                            return false;
                        }
                    }
                }else {
                    return false;
                }

            }

        } else {
            return false;
        }


        return true;
    }


    private boolean notContainsUselessPoker(ArrayList<Poker> pokerList) {

        ArrayList<Poker> uselessPokers = new ArrayList<>();
        uselessPokers.add(new Poker('2', 12));
        uselessPokers.add(new Poker('鬼', 13));
        uselessPokers.add(new Poker('王', 14));

        return !pokerList.containsAll(uselessPokers);
    }

    private boolean isOneTypeRepeat(ArrayList<Poker> oneTypePokerList, Map<Poker, Integer> repeatPokerMap, int count) {
        if (oneTypePokerList.size() == 1 && repeatPokerMap.get(oneTypePokerList.get(0)) == count) {
            this.power = oneTypePokerList.get(0).getPower() * count;
            return true;
        }
        return false;

    }

    private boolean meetTheBasiConditions(Rule lastRule) {
        return this.getRule().equals(lastRule.getRule()) && this.getPower() > lastRule.getPower();
    }


    public boolean meetConditions(Rule lastRule) {
        if (canPlayCards) {
            if (lastRule == null) { //如果是第一次出牌，lastRule是null并且出的牌符合规则，返回true
                return true;
            } else if ("王炸".equals(this.getRule())) {
                return true;
            } else if (!"炸弹".equals(lastRule.rule) && "炸弹".equals(this.rule)) {
                return true;
            } else if ("炸弹".equals(lastRule.rule) && !"炸弹".equals(this.rule)) {
                return false;
            } else if ("顺子".equals(lastRule.rule)) {
                return meetTheBasiConditions(lastRule) && this.getStraightLength() == this.getStraightLength();
            } else if ("连对".equals(lastRule.rule)) {
                return meetTheBasiConditions(lastRule) && this.getStraightLength() == lastRule.getStraightLength();
            } else if ("飞机".equals(lastRule.rule)) {
                return meetTheBasiConditions(lastRule) && this.getStraightLength() == lastRule.getStraightLength();
            }else {
                return meetTheBasiConditions(lastRule);
            }



        } else {
            return false;
        }
    }
}

