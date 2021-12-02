package org.acme;

import java.util.*;

public class Dive {

    private static boolean logEnabled = true;

    private final Strategy strategy;

    public Dive(Strategy strategy) {
        this.strategy = strategy;
    }

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public int horizontalMultipliedByDepth(List<String> input){
        input.forEach(this::calculatePositions);
        return strategy.getResult();
    }

    public void calculatePositions(final String logEntry){
        StringTokenizer stringTokenizer = new StringTokenizer(logEntry, " ");
        String direction = stringTokenizer.nextToken();
        String value = stringTokenizer.nextToken();
        switch(direction){
            case "forward":
                strategy.forward(Integer.parseInt(value));
                break;
            case "up":
                strategy.up(Integer.parseInt(value));
                break;
            case "down":
                strategy.down(Integer.parseInt(value));
                break;
            default:
                throw new IllegalArgumentException(direction);
        }

    }

}
