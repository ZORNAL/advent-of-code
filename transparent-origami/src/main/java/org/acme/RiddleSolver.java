package org.acme;

import org.acme.diagram.Diagram;
import org.acme.diagram.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RiddleSolver {

    private static void log(final String s) {
            System.out.println(s);
    }

    private long dotsAfterFirstFold = -1;
    private long dotsAfterLastFold = -1;

    public long solve(final List<String> strings){
        origami(strings);
        return dotsAfterFirstFold;
    }
    public long solve2(final List<String> strings){
        origami(strings);
        return dotsAfterLastFold;
    }

    private void origami(final List<String> strings) {
        log(strings.toString());
        final List<Point> points = strings.stream().filter(this::isCoordinate).map(this::map).collect(Collectors.toList());
        createAndDisplayBoard(points);
        final List<Fold> folds = strings.stream().filter(this::isFold).map(this::mapFold).collect(Collectors.toList());
        log(folds.toString());
        fold(points, folds);
    }

    private void createAndDisplayBoard(final List<Point> points) {
        final Integer x = points.stream().map(Point::getX).max(Integer::compareTo).orElseThrow();
        final Integer y = points.stream().map(Point::getY).max(Integer::compareTo).orElseThrow();
        log(String.format("max x = %d, max y = %d", x, y));
        final Diagram diagram = new Diagram(x + 1, y + 1);
        points.forEach(p -> diagram.update(p, 1));
        log(diagram.printBoard());
    }

    private void fold(final List<Point> points, final List<Fold> folds) {
        List<Point> temp = new ArrayList<>(points);
        for (final Fold fold: folds) {
            temp = fold(temp, fold);
            createAndDisplayBoard(temp);
            if(dotsAfterFirstFold == -1){
                dotsAfterFirstFold = temp.stream().distinct().count();
            }
        }
        if(dotsAfterLastFold == -1){
          dotsAfterLastFold = temp.stream().distinct().count();
        }
    }


    private List<Point> fold(final List<Point> points, final Fold fold) {
        final String direction = fold.getDirection();
        final int position = fold.getPosition();
        switch (direction){
            case "y":
                return points.stream().filter(point -> point.getY() != position).map(point -> {
                    if (point.getY() > position) {
                        final int distance = point.getY() - position;
                        return new Point(point.getX(),position - distance);
                    } else {
                        return point;
                    }
                }).collect(Collectors.toList());
            case "x":
                return points.stream().filter(point -> point.getX() != position).map(point -> {
                    if (point.getX() > position) {
                        final int distance = point.getX() - position;
                        return new Point(position - distance, point.getY());
                    } else {
                        return point;
                    }
                }).collect(Collectors.toList());
            default:
                throw new IllegalArgumentException();
        }
    }

    private Fold mapFold(final String s) {
        final String fold_along_ = s.replace("fold along ", "");
        final String[] split = fold_along_.split("=");
        return new Fold(split[0], split[1]);
    }

    private boolean isCoordinate(final String s) {
        return !s.isBlank() && !s.startsWith("fold");
    }

    private boolean isFold(final String s) {
        return !s.isBlank() && s.startsWith("fold");
    }

    Point map(final String s){
        final String[] split = s.split(",");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}
