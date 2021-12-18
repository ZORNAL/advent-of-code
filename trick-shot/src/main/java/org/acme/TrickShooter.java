package org.acme;

import lombok.Data;
import org.acme.diagram.Point;

import java.util.*;
import java.util.stream.Collectors;

public class TrickShooter {

    public int getDistinctHitters() {
        return distinctHitters;
    }

    private int distinctHitters;

    @Data
    static class TargetArea{

        private final int xMin;
        private final int xMax;
        private final int yMin;
        private final int yMax;

        TargetArea(final String test){
            final String[] split = test.substring(test.indexOf("x=") + 2).split("\\D+");
            xMin = Integer.parseInt(split[0]);
            xMax = Integer.parseInt(split[1]);
            yMax = - Integer.parseInt(split[2]);
            yMin = - Integer.parseInt(split[3]);
        }

        public boolean contains(final Point point){
            return point.getX() <= xMax && point.getX() >= xMin
                    && point.getY() >= yMax && point.getY() <= yMin;
        }
    }

    public int solve(final List<String> strings) {
        final TargetArea targetArea = new TargetArea(strings.get(0));

        final int minimumXAxisVelocity = findMinimumXAxisVelocity(targetArea.getXMin());
        final int maximumXAxisVelocity = targetArea.getXMax();
        final Set<Projectile> hitters = new HashSet<>();
        for (int velocity = minimumXAxisVelocity; velocity <= maximumXAxisVelocity; velocity++) {
            for (int yVelocity = targetArea.getYMax(); yVelocity < guessRough(targetArea); yVelocity++) {
                testProjectile(targetArea, hitters, velocity, yVelocity);
            }
        }
        distinctHitters = hitters.stream().map(p -> p.trajectory.get(1)).map(p -> p.getX() + "," + p.getY()).collect(Collectors.toSet()).size();
        return hitters.stream().map(Projectile::getMaxHeight).mapToInt(Integer::intValue).max().orElseThrow();
    }

    private int guessRough(final TrickShooter.TargetArea targetArea) {
        return targetArea.getYMax()*-1;
    }

    private void testProjectile(final TargetArea targetArea, final Set<Projectile> hitters, final int xVelocity, final int yVelocity) {
        final Projectile projectile = new Projectile(xVelocity, yVelocity);
        Point point = new Point(0,0);
        while (point.getY() > targetArea.getYMax()) {
            point = projectile.fly();
            if (targetArea.contains(point)) {
                hitters.add(projectile);
                break;
            }
        }
    }

    private int findMinimumXAxisVelocity(final int distance) {
        int minimumVelocity = 0;
        while(calc(minimumVelocity) < distance){
            minimumVelocity++;
        }
        return minimumVelocity;
    }

    private int calc(final int x) {
        return x * (x + 1) / 2;
    }

    public static class Projectile {
        private final List<Point> trajectory = new ArrayList<>();
        private int vx;
        private int vy;

        Projectile(final int vx, final int vy) {
            this.vx = vx;
            this.vy = vy;
            trajectory.add(new Point(0, 0));
        }

        public Point fly() {
            final Point previous = trajectory.get(trajectory.size() - 1);
            final Point newPosition = new Point(previous.getX() + vx, previous.getY() + vy);
            trajectory.add(newPosition);
            vy -= 1;
            if (vx < 0) {
                vx += 1;
            } else if (vx > 0) {
                vx -= 1;
            }
            return newPosition;
        }

        public int getMaxHeight(){
            return trajectory.stream().map(Point::getY).mapToInt(Integer::intValue).max().orElseThrow();
        }
    }

}
