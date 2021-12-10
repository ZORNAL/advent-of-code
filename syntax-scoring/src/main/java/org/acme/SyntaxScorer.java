package org.acme;

import java.util.*;

public class SyntaxScorer {

    private static final boolean logEnabled = true;

    private final Map<Character, Character> chunks = new HashMap<>();
    private final Map<Character, Integer> scoreMap = new HashMap<>();
    private final Map<Character, Integer> autoCompleteScoreMap = new HashMap<>();
    private long score = 0;
    private final List<Long> autoCompletionScoreList = new LinkedList<>();
    {
        chunks.put('(', ')');
        chunks.put('{', '}');
        chunks.put('<', '>');
        chunks.put('[', ']');
        scoreMap.put(')', 3);
        scoreMap.put('}', 1197);
        scoreMap.put('>', 25137);
        scoreMap.put(']', 57);
        autoCompleteScoreMap.put(')', 1);
        autoCompleteScoreMap.put('}', 3);
        autoCompleteScoreMap.put('>', 4);
        autoCompleteScoreMap.put(']', 2);
    }

    private static void log(final String s, final Object... o) {
        if (isLogEnabled()) {
            System.out.println(String.format(s, o));
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public long solve(final List<String> strings) {
        for (final String string : strings) {
            parse(string);
        }
        return score;
    }

    public long solve2(final List<String> strings) {
        solve(strings);
        autoCompletionScoreList.sort(Long::compareTo);
        return autoCompletionScoreList.get(autoCompletionScoreList.size()/2);
    }

    private void parse(final String line) {
        final char[] chars = line.toCharArray();
        final Stack<Character> stack = new Stack<>();
        for (final char nextCharacter : chars) {
            if (isOpening(nextCharacter)) {
                stack.push(nextCharacter);
            } else {
                final Character pop = stack.pop();
                final Character previousCharacter = chunks.get(pop);
                if (!previousCharacter.equals(nextCharacter)) {
                    log("%s - Expected %s, but found %s instead", line
                            , previousCharacter, nextCharacter);
                    calculateScore(nextCharacter);
                    return;
                }
            }
        }
        if (!stack.isEmpty()) {
            calculateAutoCompletionScore(stack);
        }
    }

    private void calculateScore(final char nextCharacter) {
        score += Optional.ofNullable(scoreMap.get(nextCharacter)).orElse(0);
    }

    private void calculateAutoCompletionScore(final Stack<Character> stack) {
        long autoCompletionScore = 0;
        final String stackBefore = stack.toString();
        while(!stack.isEmpty()){
            final Character key = chunks.get(stack.pop());
            final Integer score = autoCompleteScoreMap.get(key);
            autoCompletionScore = autoCompletionScore * 5 + score;
        }
        autoCompletionScoreList.add(autoCompletionScore);
    }

    private boolean isOpening(final char aChar) {
        return chunks.containsKey(aChar);
    }
}
