package org.acme.diagram;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DiagramTest {

    private Diagram diagram = new Diagram(10, 10);

    @Test
    public void shouldPrintBoard(){

        String board = diagram.printBoard();
        print(board);
        assertThat(board).isNotBlank();
    }

    private void print(String board) {
        System.out.println(board + "\n");
    }

    @Test
    public void shouldUpdateBoardCorrectly(){
        Point point = new Point(3, 1);
        diagram.update(point, 42L);
        print(diagram.printBoard());
        assertThat(diagram.get(point)).isEqualTo(42L);
    }

    @Test
    public void shouldFindAdjacentPoints(){
        assertThat(diagram.findAdjacent(new Point(0, 0))).containsExactlyInAnyOrder(new Point(1,0), new Point(0,1));
        assertThat(diagram.findAdjacent(new Point(9, 9))).containsExactlyInAnyOrder(new Point(9,8), new Point(8, 9));
        Point point = new Point(1, 1);
        assertThat(diagram.findAdjacent(point)).containsExactlyInAnyOrder(
                new Point(1,0),
                new Point(0, 1),
                new Point(2, 1),
                new Point(1, 2));
        diagram.update(point, 4);
        diagram.findAdjacent(point).stream().forEach(p -> diagram.update(p, 2));
        print(diagram.printBoard());
    }

    @Test
    public void shouldFindAdjacentPointsIncludingDiagonals(){
        Point point = new Point(1, 1);
        assertThat(diagram.findAdjacentIncludingDiagonals(point)).containsExactlyInAnyOrder(
                new Point(1,0),
                new Point(0, 1),
                new Point(2, 1),
                new Point(1, 2),
                new Point(2,0),
                new Point(2, 2),
                new Point(0, 0),
                new Point(0, 2));
        diagram.update(point, 4);
        diagram.findAdjacentIncludingDiagonals(point).stream().forEach(p -> diagram.update(p, 2));
        print(diagram.printBoard());
    }

}