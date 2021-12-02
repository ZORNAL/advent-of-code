package org.acme;

public class AimStrategy implements Strategy{

    int horizontalPosition = 0;
    int depth = 0;
    int aim = 0;

    @Override
    public int getResult() {
        return horizontalPosition * depth;
    }

    @Override
    public void forward(int value) {
        horizontalPosition += value;
        depth += aim * value;
    }

    @Override
    public void up(int value) {
        aim -= value;
    }

    @Override
    public void down(int value) {
        aim += value;
    }
}
