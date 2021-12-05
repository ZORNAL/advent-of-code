package org.acme;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DiveTest {

    final Dive underTest = new Dive(new DiveStrategy());

    private static final List<String> INPUT = Arrays.asList("forward 5",
            "down 5",
            "forward 8",
            "up 3",
            "down 8",
            "forward 2");

    public static final String PATH = "src/test/resources/largeInput.txt";

    @Test
    public void shouldSolveSmallPuzzleOnLevel2WithAim() {
        assertThat(new Dive(new AimStrategy()).horizontalMultipliedByDepth(INPUT)).isEqualTo(900);
    }

    @Test
    public void shouldSolveLargePuzzleOnLevel2WithAim() throws IOException {
        assertThat(new Dive(new AimStrategy()).horizontalMultipliedByDepth(readFromFile(PATH))).isEqualTo(1857958050);
    }
    @Test
    public void shouldSolveSmallPuzzle() {
        assertThat(underTest.horizontalMultipliedByDepth(INPUT)).isEqualTo(150);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(underTest.horizontalMultipliedByDepth(readFromFile(PATH))).isEqualTo(1693300);
    }

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }
}
