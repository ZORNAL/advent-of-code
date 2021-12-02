package org.acme;

public interface Strategy {

    int getResult();

    void forward(int value);
    void up(int value);
    void down(int value);
}
