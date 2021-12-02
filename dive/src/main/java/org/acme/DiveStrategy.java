package org.acme;

public class DiveStrategy implements Strategy {
    private int horizontalPosition;
    private int depth;

    public int getResult(){
        return horizontalPosition * depth;
    }

    @Override
    public void forward(int value) {
        horizontalPosition += value;
    }

    @Override
    public void up(int value) {
        depth -= value;
    }

    @Override
    public void down(int value) {
        depth += value;
    }
}
