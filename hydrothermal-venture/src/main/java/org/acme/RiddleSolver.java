package org.acme;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RiddleSolver {

    public void setConsiderDiagonals(boolean considerDiagonals) {
        this.considerDiagonals = considerDiagonals;
    }

    private boolean considerDiagonals = false;

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return true;
    }

    private final List<Line> lines = new LinkedList<>();

    public long solve(List<String> input, int size){
        input.forEach(s -> lines.add(Line.parse(s)));
        log(lines.toString());
        int[][] diagram = new int[size][size];
        lines.forEach(l -> l.draw(diagram, considerDiagonals));
        draw(diagram);
        return countDangerousLines(diagram);
    }

    private long countDangerousLines(int[][] diagram) {
        return Stream.of(diagram).flatMapToInt(Arrays::stream).filter(value -> value >= 2).count();
    }

    private void draw(int[][] diagram) {
        Stream.of(diagram).forEach(line -> log(Arrays.toString(line)));
    }

    static class Point{
        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        @Override
        public String toString() {
            return String.format("%d,%d",x,y);
        }
    }

    public static class Line{
        private final Point start;
        private final Point end;

        Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public static Line parse(String string){
            String[] split = string.trim().split("\\D+");
            return new Line(
                    new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1])),
                    new Point(Integer.parseInt(split[2]),Integer.parseInt(split[3])));
        }

        @Override
        public String toString() {
            return String.format("%s -> %s" , start, end);
        }

        public void draw(int[][] diagram, boolean considerDiagonals) {
            if(isVertical() || isHorizontal()){
                extrapolate().forEach(p -> this.add(diagram, p));
            }else{
                if(considerDiagonals){
                    extrapolateDiagonal().forEach(p -> this.add(diagram, p));
                }
            }
        }

        private List<Point> extrapolate(){
            List<Point> result = new LinkedList<>();
            IntStream streamOfX = createXStream();
            streamOfX.forEach(x -> createYStream().forEach(y -> result.add(new Point(x,y))));
            log(String.format("* Line %s extrapolated to [%s]", this, result));
            return result;
        }

        private List<Point> extrapolateDiagonal(){
            List<Point> result = new LinkedList<>();
            IntStream xStream = createXStream();
            IntStream yStream = createYStream();
            int[] xArray = xStream.toArray();
            int[] yArray = yStream.toArray();
            for (int i = 0; i < xArray.length; i++) {
                result.add(new Point(xArray[i], yArray[i]));
            }
            log(String.format("* Line %s extrapolated to [%s]", this, result));
            return result;
        }

        private IntStream createYStream() {
            IntStream range = IntStream.range(Math.min(start.getY(), end.getY()), Math.max(start.getY(), end.getY()) + 1);
            if(start.getY() > end.getY()){
                return range.boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue);
            }
            return range;

        }

        private IntStream createXStream() {
            IntStream range = IntStream.range(Math.min(start.getX(), end.getX()), Math.max(start.getX(), end.getX()) + 1);
            if(start.getX() > end.getX()){
                return range.boxed().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue);
            }
            return range;
        }

        private void add(int[][] diagram, Point start) {
            diagram[start.getY()][start.getX()] += 1;
        }

        private boolean isHorizontal() {
            return start.getY() == end.getY();
        }

        private boolean isVertical() {
            return start.getX() == end.getX();
        }
    }
}
