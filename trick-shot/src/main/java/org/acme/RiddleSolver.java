package org.acme;

import org.acme.diagram.Point;

import java.util.*;
import java.util.stream.Collectors;

public class RiddleSolver {

   private static boolean logEnabled = true;

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    static class TargetArea{

        private final int xMin;
        private final int xMax;
        private final int yMin;
        private final int yMax;

        TargetArea(String test){
            String[] split = test.substring(test.indexOf("x=") + 2).split("\\D+");
            log(Arrays.toString(split));

            xMin = Integer.parseInt(split[0]);
            xMax = Integer.parseInt(split[1]);
            yMax = - Integer.parseInt(split[2]);
            yMin = - Integer.parseInt(split[3]);

            for (int x = xMin; x <= xMax; x++) {
                for (int y = yMin; y >= yMax; y--) {
                    targetArea.add(new Point(x, -y));
                }
            }
            log("" + targetArea.size());
            log(targetArea.toString());
        }

        private final List<Point> targetArea = new LinkedList<>();

        public boolean contains(Point point){
            return targetArea.contains(point);
        }

        public int getxMin() {
            return xMin;
        }

        public int getxMax() {
            return xMax;
        }

        public int getyMin() {
            return yMin;
        }

        public int getyMax() {
            return yMax;
        }


    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public int solve(List<String> strings) {
        TargetArea targetArea = new TargetArea(strings.get(0));

        final int minimumXAxisVelocity = findMinimumXAxisVelocity(targetArea.getyMax());
        final int maximumXAxisVelocity = targetArea.getxMax();
        final Set<Projectile> hitters = new HashSet<>();
        for (int velocity = minimumXAxisVelocity; velocity <= maximumXAxisVelocity; velocity++) {
            for (int i = 0; i < 500; i++) {
                testProjectile(targetArea, velocity, hitters, i);
                testProjectile(targetArea, velocity, hitters, -i);
            }
        }
        log(hitters.stream().map(p -> p.trajectory.get(1)).map(p -> p.getX() + "," + p.getY()).collect(Collectors.toSet()).size() + "");
        return hitters.stream().map(Projectile::getMaxHeight).mapToInt(Integer::intValue).max().orElseThrow();
    }

    private void testProjectile(TargetArea targetArea, int xVelocity, Set<Projectile> hitters, int yVelocity) {
        final Projectile projectile = new Projectile(xVelocity, yVelocity);
        Point point = new Point(0,0);
        while (point.getY() > targetArea.getyMax()) {
            point = projectile.fly();
            if (targetArea.contains(point)) {
                hitters.add(projectile);
                break;
            }
        }
    }

    private int findMinimumXAxisVelocity(int distance) {
        for (int i = 0; i < 50; i++) {
            if (calc(i) > distance) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    private int calc(int x) {
        return x * (x + 1) / 2;
    }

    public static class Projectile {
        private List<Point> trajectory = new ArrayList<>();
        private int vx;
        private int vy;

        Projectile(int vx, int vy) {
            this.vx = vx;
            this.vy = vy;
            trajectory.add(new Point(0, 0));
        }

        public Point fly() {
            Point previous = trajectory.get(trajectory.size() - 1);
            Point newPosition = new Point(previous.getX() + vx, previous.getY() + vy);
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
