package org.acme;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CrapFuelCalculatorTest {

    CrapFuelCalculator underTest = new CrapFuelCalculator();

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL), true)).isEqualTo(37);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH), true)).isEqualTo(352331);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL), false)).isEqualTo(168);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH), false)).isEqualTo(99266250);
    }
}
