package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Thing {
    String version = "";
    String type = "";
    String I = "";
    final List<Thing> subs = new ArrayList<>();
    final List<String> parts = new ArrayList<>();

    private int getSize() {
        return toTechnicalString().length();
    }

    private String toTechnicalString() {
        return version + type + I + String.join("", parts);
    }

    public int sumOfVersion() {
        return RiddleSolver.getAnIntRadix2(version) + subs.stream().map(Thing::sumOfVersion).mapToInt(Integer::intValue).sum();
    }

    public int calculateSizeOfSubs() {
        return getSize() + subs.stream().map(Thing::calculateSizeOfSubs).mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        if (I.isBlank()) {
            return version + " " + type + " " + String.join(" ", parts);
        } else {
            return version + " " + type + " " + I + "\n\t" + subs.stream().map(Thing::toString).collect(Collectors.joining("\n\t"));
        }

    }
}
