package org.acme;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PassageFinderTest {

    final PassageFinder underTest = new PassageFinder(false);

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";
    public static final String PATH_TO_SLIGHTLY_LARGER = "src/test/resources/slightlyLarger.txt";
    public static final String PATH_TO_SLIGHTLY_LARGER2 = "src/test/resources/slightlyLarger2.txt";

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SMALL))).isEqualTo(10);
    }


    @Test
    public void shouldSolveSlightlyLargerPuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SLIGHTLY_LARGER))).isEqualTo(19);
    }

    @Test
    public void shouldSolveSlightlyLargerPuzzle2() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH_TO_SLIGHTLY_LARGER2))).isEqualTo(226);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.solve(readFromFile(PATH))).isEqualTo(4573);
    }

    @Test
    public void shouldSolveSmallPuzzleOnPart2() throws IOException {
        assertThat(new PassageFinder(true).solve(readFromFile(PATH_TO_SMALL))).isEqualTo(36);
    }

    @Test
    public void shouldSolveSlightlyLargerPuzzleOnPart2() throws IOException {
        assertThat(new PassageFinder(true).solve(readFromFile(PATH_TO_SLIGHTLY_LARGER))).isEqualTo(103);
    }

    @Test
    public void shouldSolveSlightlyLargerPuzzle2OnPart2() throws IOException {
        assertThat(new PassageFinder(true).solve(readFromFile(PATH_TO_SLIGHTLY_LARGER2))).isEqualTo(3509);
    }

    @Test
    public void shouldSolveLargePuzzleOnPart2() throws IOException {
        assertThat(new PassageFinder(true).solve(readFromFile(PATH))).isEqualTo(117509);
    }
}
