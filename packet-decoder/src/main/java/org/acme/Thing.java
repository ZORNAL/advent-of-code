package org.acme;

import java.util.ArrayList;
import java.util.List;

public class Thing {
    String version = "";
    String type = "";
    String I = "";
    final List<Thing> subs = new ArrayList<>();
    final List<String> parts = new ArrayList<>();

    public long sumOfVersion() {
        return RiddleSolver.getAnIntRadix2(version) + subs.stream().map(Thing::sumOfVersion).mapToLong(Long::longValue).sum();
    }

    public long calculateScore() {
        final long result;
        switch (type) {
            case "000":
                result = subs.stream().map(Thing::calculateScore)
                        .mapToLong(Long::longValue).sum();
                break;
            case "001":
                result = subs.stream().map(Thing::calculateScore)
                        .mapToLong(Long::longValue).reduce(1, Math::multiplyExact);
                break;
            case "010":
                result = subs.stream().map(Thing::calculateScore)
                        .mapToLong(Long::longValue).min().orElseThrow();
                break;
            case "011":
                result = subs.stream().map(Thing::calculateScore)
                        .mapToLong(Long::longValue).max().orElseThrow();
                break;
            case "100":
                result = RiddleSolver.getAnIntRadix2(parts.stream()
                        .map(s -> s.substring(1)).reduce("", String::concat));
                break;
            case "101":
                if (subs.get(0).calculateScore() > subs.get(1).calculateScore()) {
                    result = 1;
                } else {
                    result = 0;
                }
                break;
            case "110":
                if (subs.get(0).calculateScore() < subs.get(1).calculateScore()) {
                    result = 1;
                } else {
                    result = 0;
                }
                break;
            case "111":
                if (subs.get(0).calculateScore() == subs.get(1).calculateScore()) {
                    result = 1;
                } else {
                    result = 0;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public String toString() {
            return version + " " + type + " " + calculateScore();
    }
}
