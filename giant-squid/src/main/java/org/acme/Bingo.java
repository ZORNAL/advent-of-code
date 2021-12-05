package org.acme;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Bingo {


    private List<Board> boards;
    private int score = -1;

    public void play(List<String> input) {
        this.play(input, this::playWinnersRound);
    }

    public void play2Loose(List<String> input) {
        this.play(input, this::playLooserRound);

    }

    private Boolean playLooserRound(Integer value) {
        playRound(value);
        if (boards.size() > 1) {
            boards.removeAll(getWinnerBoards());
        } else {
            if (boards.get(0).getScore() > 0) {
                score = value * boards.get(0).getScore();
                return true;
            }
        }
        return false;
    }

    public void play(List<String> input, Function<Integer, Boolean> game) {
        final String[] numbers = input.get(0).split(",");
        this.boards = new LinkedList<>();
        log(Arrays.toString(numbers));
        createBoards(input, boards);
        printBoards(boards);
        for (String number : numbers) {
            if (game.apply(Integer.parseInt(number))) {
                return;
            }
        }
        printBoards(boards);
    }

    private boolean playWinnersRound(int value) {
        playRound(value);
        Board winner = checkWinner();
        if (winner != null) {
            log("And the winner is" + boards.toString());
            winner.calculateScore();
            score = value * winner.score;
            return true;
        }
        return false;
    }

    private Board checkWinner() {
        return boards.stream().filter(b -> b.getScore() != 0).findFirst().orElse(null);
    }

    private List<Board> getWinnerBoards() {
        return boards.stream().filter(b -> b.getScore() != 0).collect(Collectors.toList());
    }

    private void playRound(int value) {
        int round = 1;
        log(String.format("Playing round %d with value %s", round, value));
        boards.forEach(board -> board.call(value));
    }

    private void printBoards(List<Board> boards) {
        log(String.format("parsed %d boards", boards.size()));
        boards.stream().forEach(board -> {
            System.out.println(board);
            System.out.println("");
        });
    }

    private void createBoards(List<String> input, List<Board> boards) {
        Iterator<String> iterator = input.iterator();
        log("skipping numbers: " + iterator.next());

        while (iterator.hasNext()) {
            iterator.next();
            Board board = new Board(5, 5);
            board.init(
                    iterator.next().trim().split("\\s+"),
                    iterator.next().trim().split("\\s+"),
                    iterator.next().trim().split("\\s+"),
                    iterator.next().trim().split("\\s+"),
                    iterator.next().trim().split("\\s+")
            );
            boards.add(board);
        }
    }

    private void log(String toString) {
        System.out.println(toString);
    }

    public int getScore() {
        return score;
    }

    private static class Board {
        private final BoardElement[][] board;
        private int score = 0;

        public int getScore() {
            return score;
        }

        public Board(int width, int height) {
            this.board = new BoardElement[width][height];
        }

        public void init(String[]... values) {
            int width = 0;
            for (final String[] line : values) {
                int height = 0;
                for (final String value : line) {
                    this.board[width][height] = new BoardElement(value);
                    height++;
                }
                width++;
            }
        }

        @Override
        public String toString() {
            return Arrays.stream(board).map(Arrays::toString).collect(Collectors.joining("\n"));
        }

        public void call(int value) {
            if (score == 0) {
                Arrays.stream(board).forEach(line -> Arrays.stream(line).filter(boardElement -> boardElement.value == value).forEach(boardElement -> boardElement.marked = true));
                if (wins()) {
                    calculateScore();
                }
            }
        }

        public void calculateScore() {
            score = Arrays.stream(board).flatMap(Arrays::stream).filter(not(BoardElement::isMarked)).map(BoardElement::getValue).mapToInt(Integer::intValue).sum();
        }

        public boolean wins() {
            for (BoardElement[] boardElements : board) {
                boolean winningRow = true;
                for (int height = 0; height < board[0].length; height++) {
                    winningRow &= boardElements[height].isMarked();
                }
                if (winningRow) {
                    return true;
                }
            }
            for (int height = 0; height < board[0].length; height++) {
                boolean winningCol = true;
                for (BoardElement[] boardElements : board) {
                    winningCol &= boardElements[height].isMarked();
                }
                if (winningCol) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class BoardElement {
        private final int value;
        private boolean marked = false;

        public BoardElement(final String value) {
            this.value = Integer.parseInt(value);
        }

        public int getValue() {
            return value;
        }

        public boolean isMarked() {
            return marked;
        }

        @Override
        public String toString() {
            if (marked) {
                return "*" + value;
            }
            return "" + value;
        }
    }
}
