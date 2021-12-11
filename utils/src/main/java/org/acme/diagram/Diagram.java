package org.acme.diagram;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Data
public class Diagram {

    private final long[][] content;
    private final int y;
    private final int x;

    public Diagram(final int x, final int y){
        this.x = x;
        this.y = y;
        this.content = new long[y][x];
    }

    public String printBoard() {
        return Arrays.stream(content).map(Arrays::toString).collect(Collectors.joining("\n"));
    }

    public void update(final Point point, final long value) {
        content[point.getY()][point.getX()] = value;
    }

    public long get(final Point point) {
        return content[point.getY()][point.getX()];
    }

    private boolean isOutOfBounds(final Point point){
        return point.getY() < 0 || point.getX() < 0 || point.getX() >= x ||point.getY() >= y;
    }

    public List<Point> findAdjacent(final Point... point){
        List<Point> adjacentPoints = new ArrayList<>();
        Arrays.stream(point).forEach(p -> adjacentPoints.addAll(findAdjacent(p)));
        return adjacentPoints;
    }
    public List<Point> findAdjacent(final Point point){
        if(isOutOfBounds(point)){
            throw new IllegalArgumentException(point.toString());
        }
        return createListOfInboundPoints(
                createPoint(point.getX() - 1, point.getY()),
                createPoint(point.getX() + 1, point.getY()),
                createPoint(point.getX(), point.getY() - 1),
                createPoint(point.getX(), point.getY() + 1)
        );
    }

    private List<Point> createListOfInboundPoints(final Point... points) {
        return Arrays.stream(points).filter(Predicate.not(this::isOutOfBounds)).collect(Collectors.toList());
    }

    public List<Point> findAdjacentIncludingDiagonals(final Point... points){
        List<Point> adjacentPoints = new ArrayList<>();
        Arrays.stream(points).forEach(p -> adjacentPoints.addAll(findAdjacentIncludingDiagonals(p)));
        return adjacentPoints;
    }
    public List<Point> findAdjacentIncludingDiagonals(final Point point){
        if(isOutOfBounds(point)){
            throw new IllegalArgumentException(point.toString());
        }
        final List<Point> adjacent = findAdjacent(point);
        adjacent.addAll(createListOfInboundPoints(
                createPoint(point.getX() - 1, point.getY() - 1),
                createPoint(point.getX() + 1, point.getY() + 1),
                createPoint(point.getX() + 1, point.getY() - 1),
                createPoint(point.getX() - 1, point.getY() + 1)));
        return adjacent;
    }

    public List<Point> findLargerThan(long l) {
        return find(value -> value > l);
    }

    public List<Point> find(Predicate<Long> predicate) {
        List<Point> result = new ArrayList<>();
        for (int y  = 0; y < content.length; y++) {
            for (int x = 0; x < content[0].length; x++) {
                if(predicate.test(content[y][x])){
                    result.add(createPoint(x, y));
                }
            }
        }
        return result;
    }

    private Point createPoint(int x, int y) {
        return new Point(x, y);
    }

    public int size() {
        return x*y;
    }
}
