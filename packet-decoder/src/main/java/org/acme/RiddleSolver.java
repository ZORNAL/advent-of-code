package org.acme;

import java.util.*;
import java.util.stream.Collectors;

public class RiddleSolver {

    private static void log(final String s) {
            System.out.println(s);
    }

    public int solve(final List<String> strings) {
        final Stack<String> stack = createStack(hexToBin(strings.get(0)));
        final List<Thing> things = parse(Integer.MAX_VALUE, stack);
        int sumOfVersionNumbers = things.stream().map(Thing::sumOfVersion).mapToInt(Integer::intValue).sum();
        log("version: " + sumOfVersionNumbers);
        return sumOfVersionNumbers;
    }

    private Stack<String> createStack(final String bin) {
        final Stack<String> stack = new Stack<>();
        final String[] split = bin.split("");
        final List<String> collect = Arrays.stream(split).collect(Collectors.toList());
        Collections.reverse(collect);
        stack.addAll(collect);
        return stack;
    }

    private List<Thing> parse(final int max, final Stack<String> stack) {
        final ArrayList<Thing> things = new ArrayList<>();
        log(String.format("parsing string %s", stack.toString()
                .replaceAll(", ", "")
                .replaceAll("\\[", "")
                .replaceAll("]", "")));
        while(stack.contains("1") && stack.size() > 6 && things.size() <= max){
            final Thing thing = create(stack);
            things.add(thing);
            if (isLiteral(thing.type)) {
                log("found literal");
                String A = eat(5, stack);
                while (A.startsWith("1")){
                    thing.parts.add(A);
                    A = eat(5, stack);
                }
                thing.parts.add(A);
            }else{
                thing.I = eat(1, stack);
                final String length;
                if("0".equals(thing.I)){
                    length = eat(15, stack);
                    thing.parts.add(length);
                    log("found subpackages with length:" + getAnIntRadix2(length));
                    final String eat = eat(getAnIntRadix2(length), stack);
                    thing.subs.addAll(parse(Integer.MAX_VALUE, createStack(eat)));
                }else{
                    length = eat(11, stack);
                    thing.parts.add(length);
                    final int numberOf = getAnIntRadix2(length);
                    log("found subpackages with size: " + numberOf);
                    thing.subs.addAll(parse(numberOf, stack ));
                }
            }
        }
        return things;
    }

    private Thing create(final Stack<String> stack) {
        final Thing thing = new Thing();
        thing.version = eat(3, stack);
        thing.type = eat(3, stack);
        return thing;
    }

    private String eat(final int i, final Stack<String> stack) {
        final StringBuilder builder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            builder.append(stack.pop());
        }
        return builder.toString();
    }

    public static int getAnIntRadix2(final String version) {
        return Integer.parseInt(version, 2);
    }

    private boolean isLiteral(final String type) {
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
