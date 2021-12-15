package org.acme;

import lombok.Data;

@Data
public class PairInsertion {

    private final String searchPattern;
    private final String replacement;
    private final String inceptedPairInsertionA;
    private final String inceptedPairInsertionB;

    public PairInsertion(final String pairInsertion){
        final String[] split = pairInsertion.split(" -> ");
        this.searchPattern = split[0];
        this.replacement = split[1];
        inceptedPairInsertionA = searchPattern.charAt(0) + replacement;
        inceptedPairInsertionB = replacement + searchPattern.charAt(1);
    }
}
