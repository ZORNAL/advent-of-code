package org.acme;

import java.util.*;
import java.util.stream.Collectors;

public class LanternfishSolver {

    private static boolean logEnabled = true;

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public long solve(String string, int days){
        List<Integer> swarm = Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        final long[] buckets = new long[9];
        swarm.forEach(s-> buckets[s]++);
        log("Initial state: " + Arrays.toString(buckets));
        for (int day = 1; day <= days; day++) {
            long newCycle = buckets[0];
            buckets[0] = buckets[1];
            buckets[1] = buckets[2];
            buckets[2] = buckets[3];
            buckets[3] = buckets[4];
            buckets[4] = buckets[5];
            buckets[5] = buckets[6];
            buckets[6] = buckets[7];
            buckets[7] = buckets[8];
            buckets[6] += newCycle;
            buckets[8] = newCycle;
            log(String.format("Day %d having %d fish: %s", day, Arrays.stream(buckets).sum(), Arrays.toString(buckets)));
        }
        return Arrays.stream(buckets).sum();
    }

}
