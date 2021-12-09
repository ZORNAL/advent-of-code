package org.acme;

import java.util.*;
import java.util.stream.Collectors;

public class SevenSegmentDecoder {


    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return true;
    }

    public long solve(final List<String> lines, final boolean completeSolution) {
        long result = 0;
        for (final String line : lines) {
            result += analyse(line, completeSolution);
        }
        return result;
    }

    private long analyse(final String line, final boolean completeSolution) {
        final String[] paresResult = line.split("\\|");
        final List<String> sortedSignals = getSorted(paresResult[0]);
        final List<String> sortedOutput = getSorted(paresResult[1]);
        log("usp: " + sortedSignals);
        log("os: " + sortedOutput);

        if (completeSolution) {
            final Map<String, Integer> translatedecodedSignals = decode(sortedSignals);
            final String collect = sortedOutput.stream()
                    .map(translatedecodedSignals::get)
                    .map(Object::toString).collect(Collectors.joining(""));
            return Integer.parseInt(collect);
        } else {
            return sortedOutput.stream()
                    .filter(this::filter)
                    .count();
        }
    }

    private List<String> getSorted(final String outputSignalPattern) {
        final String[] output = outputSignalPattern.trim().split(" ");
        return sortSignalsAlphabetically(output);
    }

    private List<String> sortSignalsAlphabetically(final String[] output) {
        return Arrays.stream(output)
                .map(this::sort).collect(Collectors.toList());
    }

    private Map<String, Integer> decode(final List<String> signals) {
        final String one = signals.stream().filter(this::isOne).findFirst().orElseThrow();
        final String four = signals.stream().filter(this::isFour).findFirst().orElseThrow();
        final List<String> strings = new LinkedList<>(signals);
        final Map<String, Integer> translation = new HashMap<>();
        strings.remove(put(one, translation, 1));
        strings.remove(put(four, translation, 4));
        strings.remove(put(signals.stream().filter(this::isSeven).findFirst().orElseThrow(), translation, 7));
        strings.remove(put(signals.stream().filter(this::isEight).findFirst().orElseThrow(), translation, 8));
        final String nine = findNine(strings, four);
        strings.remove(put(nine, translation, 9));
        strings.remove(put(findNine(strings, one), translation, 0));
        strings.remove(put(findSix(strings), translation, 6));
        strings.remove(put(findThree(strings, one), translation, 3));
        strings.remove(put(findFive(strings, nine), translation, 5));
        strings.remove(put(strings.get(0), translation, 2));
        return translation;
    }

    private String put(final String one, final Map<String, Integer> translation, final int i) {
        translation.put(sort(one), i);
        log(i + " is: " + one);
        return one;
    }

    private String sort(final String one) {
        final char[] a = one.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }

    private String findFive(final List<String> strings, final String nine) {
        return strings.stream().filter(s -> containsAll(nine, s)).findFirst().orElseThrow();
    }

    private String findThree(final List<String> strings, final String one) {
        return strings.stream().filter(s -> containsAll(s, one)).findFirst().orElseThrow();
    }

    boolean containsAll(final String testString, final String characters){
        final char[] chars = characters.toCharArray();
        for (final char aChar : chars) {
            if (testString.indexOf(aChar) < 0) {
                return false;
            }
        }
        return true;
    }

    private String findSix(final List<String> strings) {
        return strings.stream().filter(s -> s.length() == 6).findFirst().orElseThrow();
    }

    private String findNine(final List<String> strings, final String one) {
        return strings.stream().filter(s -> s.length() == 6).filter(s -> containsAll(s, one)).findFirst().orElseThrow();
    }

    private boolean filter(final String signal) {
        return isOne(signal) || isFour(signal) || isSeven(signal) || isEight(signal);
    }

    private boolean isEight(final String signal) {
        return signal.length() == 7;
    }

    private boolean isSeven(final String signal) {
        return signal.length() == 3;
    }

    private boolean isFour(final String signal) {
        return signal.length() == 4;
    }

    private boolean isOne(final String signal) {
        return signal.length() == 2;
    }

}
