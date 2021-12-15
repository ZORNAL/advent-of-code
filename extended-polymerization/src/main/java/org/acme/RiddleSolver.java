package org.acme;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RiddleSolver {

    private static boolean logEnabled = true;
    private final int rounds;
    private Map<String, PairInsertion> pairInsertionByPattern;

    public RiddleSolver(final int rounds) {
        this.rounds = rounds;
    }

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public long solve2(final List<String> strings) {
        final String template = getTemplate(strings);
        final List<PairInsertion> pairInsertions = getPairInsertions(strings);
        log("pair insertions: " + pairInsertions);
        log("Template: " + template);
        pairInsertionByPattern = pairInsertions.stream().collect(Collectors.toMap(PairInsertion::getSearchPattern, Function.identity()));
        Map<PairInsertion, Long> collect = new HashMap<>();
        init(collect, filterApplicableInsertionPairs(template, pairInsertions));
        final Map<String, Long> result = createResult(template);
        collect.forEach((key, value) -> increment(result, key.getReplacement(), value));

        for (int round = 0; round < rounds - 1; round++) {
            log("Round: " + round);
            final Map<PairInsertion, Long> inserts = new HashMap<>();
            collect.entrySet().stream().filter(es -> es.getValue() > 0).forEach(
                    e-> doit(e, inserts));
            inserts.forEach((key, value) -> increment(result, key.getReplacement(), value));
            collect = inserts;
        }
        log(result.toString());
        final Long max = result.values().stream().max(Long::compareTo).orElseThrow();
        final Long min = result.values().stream().min(Long::compareTo).orElseThrow();
        return max - min;
    }

    private void init(final Map<PairInsertion, Long> collect, final List<PairInsertion> applicableInsertionPairs) {
        applicableInsertionPairs.forEach(s -> increment(collect, s));
    }

    private Map<String, Long> createResult(final String template) {
        final Map<String, Long> result = new HashMap<>();
        final String[] split = template.split("");
        for (String s : split) {
            increment(result, s);
        }
        return result;
    }

    private void doit(final Map.Entry<PairInsertion, Long> entry, final Map<PairInsertion, Long> inserts) {
        increment(inserts, entry.getKey().getInceptedPairInsertionA(), entry.getValue());
        increment(inserts, entry.getKey().getInceptedPairInsertionB(), entry.getValue());
    }

    private void increment(final Map<PairInsertion, Long> inserts, final String a, final Long value) {
        final PairInsertion pairInsertion = pairInsertionByPattern.get(a);
        if (pairInsertion != null) {
            increment(inserts, pairInsertion, value);
        }
    }

    private <T> void increment(final Map<T, Long> inserts, final T pairInsertion) {
        increment(inserts, pairInsertion, 1L);
    }

    private <T> void increment(final Map<T, Long> inserts, final T pairInsertion, final Long value) {
        log(String.format("incrementing %s by %d", pairInsertion, value));
        if(!inserts.containsKey(pairInsertion)){
            inserts.put(pairInsertion, value);
        }else{
            inserts.put(pairInsertion, inserts.get(pairInsertion) + value);
        }
    }

    private List<PairInsertion> getPairInsertions(final List<String> strings) {
        return strings.stream().filter(this::isPairInsertion).map(PairInsertion::new).collect(Collectors.toList());
    }

    private String getTemplate(final List<String> strings) {
        return strings.stream().filter(this::isTemplate).findFirst().orElseThrow();
    }

    private List<PairInsertion> filterApplicableInsertionPairs(final String template, final List<PairInsertion> pairInsertions) {
        final List<PairInsertion> result = new ArrayList<>();
        for (final PairInsertion pairInsertion : pairInsertions) {
            final int i = StringUtils.countMatches(template, pairInsertion.getSearchPattern());
            for (int index = 0; index < i; index++) {
                result.add(pairInsertion);
            }
        }
        return result;
    }

    private boolean isTemplate(final String s) {
        return !s.isBlank() && !isPairInsertion(s);
    }

    private boolean isPairInsertion(final String s) {
        return !s.isBlank() && s.contains(" -> ");
    }


}
