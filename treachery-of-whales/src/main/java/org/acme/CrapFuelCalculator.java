package org.acme;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CrapFuelCalculator {

    private static boolean logEnabled = false;

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public int solve(List<String> strings, boolean simple) {
        String firstLine = strings.get(0);
        log(firstLine);
        Integer[] positions = Arrays.stream(firstLine.split(",")).map(Integer::parseInt).collect(Collectors.toList()).toArray(Integer[]::new);
        int length = positions.length;
        log("possible alignments: " + length);
        int minimum = Integer.MAX_VALUE;
        for (int index = 0; index < length; index++) {
            if(simple){
                minimum = Math.min(minimum, alignOnIndex(index, positions, this::calculateSimple));
            }else {
                minimum = Math.min(minimum, alignOnIndex(index, positions, this::calculateConsumption));
            }
        }
        return minimum;
    }

    private int alignOnIndex(int index, Integer[] positions, BiFunction<Integer, Integer, Integer> calculation) {
        int overallConsumption = 0;
        for (Integer position : positions) {
            int consumption = calculation.apply(index, position);
            overallConsumption += consumption;
            log(String.format("Move from %d to %d: %d fuel", position, index, consumption));
        }
        return overallConsumption;
    }

    private int calculateConsumption(int target, Integer position) {
        int n = Math.abs(target - position);
        return (n + 1) * n / 2;
    }

    private int calculateSimple(int target, Integer position) {
        return Math.abs(target - position);
    }

}
