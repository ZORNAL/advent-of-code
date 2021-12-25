package org.acme;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RiddleSolver {

    private static boolean logEnabled = true;

    Set<String> ons = new HashSet<>();

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }


    public int solve(List<String> strings) {
        List<Round> rounds = strings.stream().map(Round::parse).collect(Collectors.toList());
        rounds.stream().filter(Round::isInBound).forEach(this::switchCubeoits);
        return ons.size();
    }

    public void switchCubeoits(Round round) {

        int[] x = round.getFilteredX();
        int[] y = round.getFilteredY();
        int[] z = round.getFilteredZ();
        for (int i = x[0]; i <= x[1]; i++) {
            for (int j = y[0]; j <= y[1]; j++) {
                for (int k = z[0]; k <= round.getZ()[1]; k++) {
                    if (inRange(i) && inRange(j) && inRange(k)) {
                        final String string = String.format("%d,%d,%d", i, j, k);
                        if (round.isOn()) {
                            ons.add(string);
                        } else {
                            ons.remove(string);
                        }
                    }
                }
            }
        }
    }

    private boolean inRange(int i) {
        return i >= -50 && i <= 50;
    }

    @Data
    static class Round {
        public static final int[] NO_INTS = {-1, -2};
        private boolean on = false;
        private int[] x = new int[2];
        private int[] y = new int[2];
        private int[] z = new int[2];

        public static Round parse(String s) {

            Round round = new Round();

            if (s.startsWith("on")) {
                round.setOn(true);
            }

            String substring = s.substring(s.indexOf("x="));
            String s1 = substring.replaceAll("\\w+=", "");
            String[] split = s1.split(",");
            fill(round.getX(), split[0]);
            fill(round.getY(), split[1]);
            fill(round.getZ(), split[2]);
            System.out.println(round);
            return round;
        }

        private static void fill(int[] x, String xRange1) {
            String xRange = xRange1;
            String[] split1 = xRange.split("\\.\\.");
            x[0] = Integer.parseInt(split1[0]);
            x[1] = Integer.parseInt(split1[1]);
        }

        public int[] getFilteredX() {
            return getFiltered(x);
        }

        private int[] getFiltered(int[] x) {
            if (x[0] >= 50 || x[1] <= -50) {
                return NO_INTS;
            }
            return new int[]{Math.max(-50, x[0]), Math.min(50, x[1])};
        }

        public boolean isInBound(){
            return getFilteredX() != NO_INTS && getFilteredY() != NO_INTS && getFilteredZ() != NO_INTS;
        }

        public int[] getFilteredY() {
            return getFiltered(y);
        }

        public int[] getFilteredZ() {
            return getFiltered(z);
        }

        public String toString() {
            return "RiddleSolver.Round(on=" + this.isOn() + ", " +
                    "x=" + java.util.Arrays.toString(this.getX()) + ", y=" + java.util.Arrays.toString(this.getY()) + ", z=" + java.util.Arrays.toString(this.getZ()) + ", " +
                    "xf=" + java.util.Arrays.toString(this.getFilteredX()) + ", yf=" + java.util.Arrays.toString(this.getFilteredY()) + ", zf=" + java.util.Arrays.toString(this.getFilteredZ()) + ")";
        }
    }


}
