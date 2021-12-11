package org.acme;

import org.acme.diagram.Diagram;
import org.acme.diagram.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RiddleSolver {

    private static final int ROUNDS = 100;

    private static void log(final String s, final Object... params) {
        System.out.printf(s + "\n", params);
    }

    public int solve(final List<String> strings) {
        final Diagram board = createBoard(strings);
        log(board.printBoard());
        return playRounds(board);
    }

    private int playRounds(final Diagram board) {
        int score = 0;
        for (int round = 1; round <= ROUNDS; round++) {
            score += playRound(board, round);
        }
        return score;
    }

    private int playRound(final Diagram board, final int round) {
        log("playing round %d", round);
        updateEnergyLevel(board);
        List<Point> aboutToFlash = board.findLargerThan(9L);
        final Set<Point> flashingInThisRound = new HashSet<>(aboutToFlash);
        while (!aboutToFlash.isEmpty()) {
            final List<Point> adjacent = board.findAdjacentIncludingDiagonals(aboutToFlash.toArray(new Point[0]));
            updateEnergyLevel(board, adjacent);
            aboutToFlash = board.findLargerThan(9L);
            aboutToFlash.removeAll(flashingInThisRound);
            flashingInThisRound.addAll(aboutToFlash);
        }
        flash(board, new ArrayList<>(flashingInThisRound));
        log("%d flashes in round %d", flashingInThisRound.size(), round);
        return flashingInThisRound.size();
    }

    private void updateEnergyLevel(final Diagram board) {
        final List<Point> largerThan = board.findLargerThan(-1);
        updateEnergyLevel(board, largerThan);
    }

    private void updateEnergyLevel(final Diagram board, final List<Point> largerThan) {
        for (final Point point : largerThan) {
            board.update(point, board.get(point) + 1);
        }
    }

    private void flash(final Diagram board, final List<Point> aboutToFlash) {
        for (final Point point : aboutToFlash) {
            board.update(point, 0);
        }
    }

    private Diagram createBoard(final List<String> strings) {
        final Diagram diagram = new Diagram(strings.size(), strings.get(0).length());
        for (int y = 0; y < strings.size(); y++) {
            final String line = strings.get(y);
            final String[] split = line.split("");
            for (int x = 0; x < split.length; x++) {
                final Point point = new Point(x, y);
                diagram.update(point, Long.parseLong(split[x]));
            }
        }
        return diagram;
    }

    public int findSynchronizedBoard(final List<String> strings) {
        final Diagram board = createBoard(strings);
        log(board.printBoard());
        int round = 0;
        while (isNotInSync(board)) {
            playRound(board, round);
            round++;
        }
        log("Board on round %d", round);
        log(board.printBoard());
        return round;
    }

    private boolean isNotInSync(final Diagram board) {
        final long checkValue = board.get(new Point(0, 0));
        final List<Point> points = board.find(value -> value == checkValue);
        return points.size() < board.size();
    }


}
