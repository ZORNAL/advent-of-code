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

    private final RiddleSolver underTest = new RiddleSolver(10);

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        assertThat(underTest.solve2(readFromFile(PATH_TO_SMALL))).isEqualTo(1588);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.solve2(readFromFile(PATH))).isEqualTo(2223);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() throws IOException {
        assertThat( new RiddleSolver(40).solve2(readFromFile(PATH_TO_SMALL))).isEqualTo(2188189693529L);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() throws IOException {
        assertThat( new RiddleSolver(40).solve2(readFromFile(PATH))).isEqualTo(2566282754493L);
    }
}
