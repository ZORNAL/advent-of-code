package org.acme;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class RiddleSolverTest {

    final RiddleSolver underTest = new RiddleSolver();

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL), 10)).isEqualTo(5);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH), 1000)).isEqualTo(8350);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() throws IOException {
        underTest.setConsiderDiagonals(true);
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL), 10)).isEqualTo(12);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() throws IOException {
        underTest.setConsiderDiagonals(true);
        assertThat(underTest.solve(readFromFile(PATH), 1000)).isEqualTo(19374);
    }
}
