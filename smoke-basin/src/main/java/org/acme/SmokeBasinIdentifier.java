package org.acme;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class SmokeBasinIdentifier {

    private static void log(final String s, final Object... params) {
        if (isLogEnabled()) {
            System.out.println(String.format(s, params));
        }
    }

    public static boolean isLogEnabled() {
        return true;
    }

    public int solve(final List<String> strings) {
        final int[][] heights = getHeightMap(strings);
        final List<Point> points = calculateDips(heights);
        return calculateRiskLevel(points, heights);
    }

    private int calculateRiskLevel(final List<Point> points, final int[][] heights) {
        int riskLevel = 0;
        for (final Point point: points) {
            riskLevel += 1 +  heights[point.getRow()][point.getColumn()];
        }
        return riskLevel;
    }

    private int[][] getHeightMap(final List<String> strings) {
        final int[][] heights = new int[strings.size()][strings.get(0).length()];
        for (int column = 0; column < strings.size(); column++) {
            final String line = strings.get(column);
            final char[] chars = line.toCharArray();
            for (int row = 0; row < chars.length; row++) {
                heights[column][row] = Integer.parseInt("" + chars[row]);
            }
        }
        return heights;
    }

    private void printHeightMap(final int[][] heights) {
        Arrays.stream(heights).forEach(s -> System.out.println(Arrays.toString(s)));
    }

    private List<Point> calculateDips(final int[][] heights) {
        log("calculate dips for: ");
        printHeightMap(heights);

        final int rowCount = heights.length;
        final int columnCount = heights[0].length;

        final List<Point> dips = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                final int height = heights[row][column];
                if (height < getElement(heights, row - 1, column) &&
                        height < getElement(heights, row + 1, column) &&
                        height < getElement(heights, row, column - 1) &&
                        height < getElement(heights, row, column + 1)) {
                    dips.add(new Point(row, column));
                }
            }
        }
        printHeightMap(heights);
        return dips;
    }

    private int getElement(final int[][] heights, final int row, final int column) {
        final int rowCount = heights.length;
        final int columnCount = heights[0].length;
        if (row >= 0 && row < rowCount && column >= 0 && column < columnCount) {
            return heights[row][column];
        }
        return 10;
    }

    public int solve2(final List<String> lines) {
        final int[][] heights = getHeightMap(lines);
        final List<Point> points = calculateDips(heights);
        log("found %d dips", points.size());

        final List<Set<Point>> list = new LinkedList<>();
        for (final Point point: points) {
            final Set<Point> set = eat(heights, point, new HashSet<>());
            log(set.toString());
            list.add(set);
        }
        final List<Integer> collect = list.stream().map(Set::size).sorted(Integer::compareTo).collect(Collectors.toList());
        Collections.reverse(collect);
        return collect.get(0) * collect.get(1) * collect.get(2);
    }

    private Set<Point> eat(final int[][] heights, final Point point, final Set<Point> visitedPoints) {
        final int value = getElement(heights, point.getRow(), point.getColumn());
        if(value < 9){
            visitedPoints.add(point);
            final List<Point> points = createAdjacentPoints(point).stream().filter(not(visitedPoints::contains)).collect(Collectors.toList());
            for (final Point next: points) {
                visitedPoints.addAll(eat(heights, next, visitedPoints));
            }
            return visitedPoints;
        }
        return Collections.emptySet();
    }

    private List<Point> createAdjacentPoints(final Point point) {
        final int row = point.getRow();
        final int column = point.getColumn();
        final List<Point> points = new ArrayList<>();
        points.add(new Point(row - 1, column));
        points.add(new Point(row, column - 1));
        points.add(new Point(row + 1, column));
        points.add(new Point(row, column + 1));
        return points;
    }

    private static class Point {
        private final int row;
        private final int column;

        public Point(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return "{" + row + "," + column + '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Point point = (Point) o;
            return row == point.row && column == point.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
