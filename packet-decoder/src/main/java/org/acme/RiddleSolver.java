package org.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RiddleSolver {

    private static boolean logEnabled = true;

    private int sumOfVersionNumbers = 0;
    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public int solve(List<String> strings) {
        String s = strings.get(0);
        String bin = hexToBin(s);
        List<Thing> things = parse2(bin, Integer.MAX_VALUE);
        log(things.toString());
        sumOfVersionNumbers = things.stream().map(Thing::sumOfVersion).mapToInt(Integer::intValue).sum();
        log("version: " + sumOfVersionNumbers);
        return sumOfVersionNumbers;
    }


    public static class Thing{
        String version = "";
        String type = "";
        String I = "";
        List<Thing> subs = new ArrayList<>();
        List<String> parts = new ArrayList<>();

        private int getSize(){
            return toTechnicalString().length();
        }

        private String toTechnicalString() {
            return version + type + I + parts.stream().collect(Collectors.joining(""));
        }

        public int sumOfVersion(){
            return getAnIntRadix2(version) + subs.stream().map(Thing::sumOfVersion).mapToInt(Integer::intValue).sum();
        }
        public int calculateSizeOfSubs() {
            return getSize() + subs.stream().map(Thing::calculateSizeOfSubs).mapToInt(Integer::intValue).sum();
        }

        @Override
        public String toString() {
            if(I.isBlank()){
                return version + " " + type + " " + parts.stream().collect(Collectors.joining(" "));
            }else{
                return version + " " + type + " " + I + "\n\t" + subs.stream().map(Thing::toString).collect(Collectors.joining("\n\t"));
            }

        }
    }
    private List<Thing> parse2(String bin, int max) {
        ArrayList<Thing> things = new ArrayList<>();
        log(String.format("parsing string %s",bin));
        int startIndex = 0;
        while(startIndex < bin.length() && bin.substring(startIndex).contains("1") && things.size() <= max){
            // log("continuing with: " + bin.substring(startIndex));
            RiddleSolver.Thing thing = create(bin, startIndex);
            things.add(thing);
            if (isLiteral(thing.type)) {
                log("found literal");
                int index;
                for (index = 6; index < bin.length(); index=index+5) {
                    String A = bin.substring(startIndex + index, startIndex + index + 5);
                    thing.parts.add(A);
                    if(A.startsWith("0")){
                        break;
                    }
                }
                startIndex = startIndex + index + 5;
            }else{
                thing.I = bin.substring(startIndex + 6, startIndex + 7);
                String length;
                final int offset;
                if("0".equals(thing.I)){
                    offset = 15;
                    length = bin.substring(startIndex + 7, startIndex + 7 + offset);
                    thing.parts.add(length);
                    log("found subpackages with length:" + getAnIntRadix2(length));
                    int endIndex = startIndex + 7 + offset + getAnIntRadix2(length);
                    thing.subs.addAll(parse2(bin.substring(startIndex + 7 + offset, endIndex), Integer.MAX_VALUE));
                    startIndex = endIndex;
                }else{
                    offset = 11;
                    length = bin.substring(startIndex + 7, startIndex + 7 + offset);
                    thing.parts.add(length);
                    int numberOf = getAnIntRadix2(length);
                    log("found subpackages with size: " + numberOf);
                    String substring = bin.substring(startIndex + 7 + offset);
                    thing.subs.addAll(parse2(substring, numberOf));
                    startIndex = startIndex + offset + thing.calculateSizeOfSubs();
                }
            }
        }
        return things;
    }

    private Thing create(String bin, int startIndex) {
        Thing thing = new Thing();
        thing.version = bin.substring(startIndex, startIndex + 3);
        thing.type = bin.substring(startIndex + 3, startIndex + 6);
        return thing;
    }

    private void parse(String bin) {
        log(String.format("parsing string %s",bin));
        if(!bin.contains("1")){
            return;
        }
        int startIndex = 0;
        String version = bin.substring(startIndex, startIndex + 3);
        String type = bin.substring(startIndex + 3, startIndex + 6);
        sumOfVersionNumbers += getAnIntRadix2(version);
        log("v: " + getAnIntRadix2(version));
        if (isLiteral(type)) {
            log("found literal");
            String literalValue = "";
            int index;
            for (index = 6; index < bin.length(); index=index+5) {
                String A = bin.substring(startIndex + index, startIndex + index + 5);
                String literalPart = A.substring(1);
                literalValue = literalValue + literalPart;
                if(A.startsWith("0")){
                    break;
                }
            }
            parse(bin.substring(startIndex + index + 5));
        } else {
            String I = bin.substring(startIndex + 6, startIndex + 7);
            String length;
            final int offset;
            if("0".equals(I)){
                log("found subpackages with length");
                offset = 15;
                length = bin.substring(startIndex + 7, startIndex + 7 + offset);
                log("I: " + getAnIntRadix2(I));
                log("Length: " + getAnIntRadix2(length));
                parse(bin.substring(startIndex + 7 + offset));//, startIndex + 7 + offset + getAnIntRadix2(length)));
            }else{
                log("found subpackages with size");
                offset = 11;
                // int numberOf = getAnIntRadix2(bin.substring(startIndex + 7, startIndex + 7 + offset));
                String substring = bin.substring(startIndex + 7 + offset);
                parse(substring);
            }

        }


    }

    private static int getAnIntRadix2(String version) {
        return Integer.parseInt(version, 2);
    }

    private boolean isLiteral(String type) {
        return "100".equals(type);
    }

    private String hexToBin(String hex) {
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }
}
