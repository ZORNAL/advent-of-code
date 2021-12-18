package org.acme;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TrickShooterTest {

    final TrickShooter underTest = new TrickShooter();

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL))).isEqualTo(45);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH))).isEqualTo(3570);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() throws IOException {
        underTest.solve(readFromFile(PATH_TO_SMALL));
        assertThat(underTest.getDistinctHitters()).isEqualTo(112);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() throws IOException {
        underTest.solve(readFromFile(PATH));
        assertThat(underTest.getDistinctHitters()).isEqualTo(1919);
    }
}