package org.acme;

import java.util.*;

public class SonarSweep {


    public static List<Integer> convert(List<Integer> input){
        final Iterator<Integer> iterator = input.iterator();
        final List<Integer> result = new LinkedList<>();
        final Deque<Integer> queue = new ArrayDeque<>();
        while (iterator.hasNext()){
            queue.add(iterator.next());
            if(hasThreeElements(queue)){
                Integer sum = queue.stream()
                        .reduce(0, Integer::sum);
                result.add(sum);
                queue.removeFirst();
            }
        }
        return result;
    }

    private static boolean hasThreeElements(Deque<Integer> queue) {
        return queue.size() == 3;
    }

    public static int analyse(final List<Integer> input) {
        int largerCount = 0;
        final Iterator<Integer> iterator = input.iterator();
        Integer previous = iterator.next();
        log(previous + " (N/A - no previous measurement)");
        while (iterator.hasNext()) {
            final Integer next = iterator.next();
            largerCount = isLarger(largerCount, previous, next);
            previous = next;
        }
        return largerCount;
    }

    private static int isLarger(int larger, final Integer previous, final Integer next) {
        if (previous > next) {
            log(next + " (decreased)");
        } else if (previous < next) {
            log(next + " (increased)");
            larger++;
        } else {
            log(next + " (no change)");
        }
        return larger;
    }

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return false;
    }

}
