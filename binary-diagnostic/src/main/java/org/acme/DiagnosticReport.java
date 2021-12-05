package org.acme;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiagnosticReport {

    private final int digits;
    private final int[] calculation;
    private int gammaRate;
    private int epsilonRate;

    DiagnosticReport(int digits) {
        this.digits = digits;
        calculation = new int[digits];
    }

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return true;
    }

    public int getPowerConsumption(List<String> input) {
        input.forEach(this::collect);
        log(Arrays.toString(calculation));
        calculate(input.size());
        return gammaRate * epsilonRate;
    }

    private void calculate(int size) {
        int halfSize = size / 2;
        String collect = Arrays.stream(calculation).boxed().map((i) -> map(i, halfSize)).collect(Collectors.joining(""));
        log("gamma rate: " + collect);
        this.gammaRate = Integer.parseInt(collect, 2);
        int mask = (int) Math.round(Math.pow(2, digits) - 1);
        log("mask: " + Integer.toString(mask, 2));
        this.epsilonRate = gammaRate ^ mask;
        log("epsilon rate: " + Integer.toString(epsilonRate, 2));
    }

    public String map(int i, int halfSize) {
        if (i >= halfSize) {
            return "1";
        } else {
            return "0";
        }
    }

    private void collect(final String token) {
        IntStream.iterate(0, i -> i + 1).limit(digits).forEach(index -> calculatePosition(token, index));
    }

    private void calculatePosition(String token, int index) {
        String substring = token.substring(index, index + 1);
        calculation[index] = calculation[index] + Integer.parseInt(substring);
    }

    public int verifyLifeSupportRating(List<String> input) {

        Integer oxygen = calculateOxygenGeneratorRating(input, true);
        log("calculate oxygen: "+ oxygen);
        Integer co2 = calculateOxygenGeneratorRating(input, false);
        log("calculate co2: "+ co2);
        return oxygen * co2;
    }

    private Integer calculateOxygenGeneratorRating(List<String> input, boolean oxygen) {
        List<Integer> collect = toIntegerList(input);
        logCollection(collect);
        for (int index = digits; index > 0; index--) {
            if (collect.size() == 1) {
                break;
            }
            int position = index - 1;
            final int bit;
            if(!oxygen){
                bit = determineBit(collect.size(), countBitsOnPosition(collect, position)) ^ 1;
            }else {
                bit = determineBit(collect.size(), countBitsOnPosition(collect, position));
            }
            log("current bit " + bit + " - current position " + position);
            collect = collect.stream().filter(i -> getBit(i, position) == bit).collect(Collectors.toList());
            logCollection(collect);
        }
        return collect.get(0);
    }

    private void logCollection(List<Integer> collect) {
        log(collect.stream().map(i -> Integer.toString(i, 2)).map(s -> leadingZeros(s, digits)).collect(Collectors.toList()).toString());
    }


    private List<Integer> toIntegerList(List<String> input) {
        return input.stream().map(i -> Integer.parseInt(i, 2)).collect(Collectors.toList());
    }

    public String leadingZeros(String s, int length) {
        if (s.length() >= length) return s;
        else return String.format("%0" + (length-s.length()) + "d%s", 0, s);
    }

    private Integer countBitsOnPosition(List<Integer> collect, int position) {
        Integer sum = collect.stream().map(i -> getBit(i, position)).reduce(0, Integer::sum);
        log(String.format("counting %d on position %d", sum, position));
        return sum;
    }

    private int determineBit(int size, int sum) {
        final int bit;
        if(sum * 2 >= size){
            bit = 1;
        }else{
            bit = 0;
        }
        return bit;
    }

    int getBit(int n, int k) {
        return (n >> k) & 1;
    }
}