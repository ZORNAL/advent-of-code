package org.acme;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanternfishSolverTest {

    public static final String SMALL = "3,4,3,1,2";
    public static final String LARGE = "1,1,3,5,1,1,1,4,1,5,1,1,1,1,1,1,1,3,1,1,1,1,2,5,1,1,1,1,1,2,1,4,1,4,1,1,1,1,1,3,1,1,5,1,1,1,4,1,1,1,4,1,1,3,5,1,1,1,1,4,1,5,4,1,1,2,3,2,1,1,1,1,1,1,1,1,1,1,1,1,1,5,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,5,1,1,1,3,4,1,1,1,1,3,1,1,1,1,1,4,1,1,3,1,1,3,1,1,1,1,1,3,1,5,2,3,1,2,3,1,1,2,1,2,4,5,1,5,1,4,1,1,1,1,2,1,5,1,1,1,1,1,5,1,1,3,1,1,1,1,1,1,4,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,3,2,1,1,1,1,2,2,1,2,1,1,1,5,5,1,1,1,1,1,1,1,1,1,1,1,1,2,2,1,1,4,2,1,4,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,1,5,1,1,1,1,1,1,1,1,3,1,1,3,3,1,1,1,3,5,1,1,4,1,1,1,1,1,4,1,1,3,1,1,1,1,1,1,1,1,2,1,5,1,1,1,1,1,1,1,1,1,1,4,1,1,1,1";
    LanternfishSolver underTest = new LanternfishSolver();

    @Test
    public void shouldSolveSmallPuzzle() {
        assertThat(underTest.solve(SMALL, 80)).isEqualTo(5934);
    }

    @Test
    public void shouldSolveLargePuzzle() {
        assertThat(underTest.solve(LARGE, 80)).isEqualTo(396210);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() {
        assertThat(underTest.solve(SMALL, 256)).isEqualTo(26984457539L);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() {
        assertThat(underTest.solve(LARGE, 256)).isEqualTo(1770823541496L);
    }
}
