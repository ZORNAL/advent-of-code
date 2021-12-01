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

public class SonarSweepTest {
    private static final List<Integer> INPUT = Arrays.asList(199,
            200,
            208,
            210,
            200,
            207,
            240,
            269,
            260,
            263);
    public static final String PATH = "src/test/resources/largeInput.txt";

    @Test
    public void shouldSolveSmallPuzzle() {
        assertThat(SonarSweep.analyse(INPUT)).isEqualTo(7);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        assertThat(SonarSweep.analyse(readFromFile(PATH))).isEqualTo(1713);
    }

    @Test
    public void shouldSolveLargePuzzleOnLevel2() throws IOException {
        assertThat(SonarSweep.analyse(SonarSweep.convert(readFromFile(PATH)))).isEqualTo(1734);
    }

    @Test
    public void shouldConvertCorrectly() {
        assertThat(SonarSweep.convert(INPUT)).containsExactly(607, 618, 618, 617, 647, 716, 769, 792);
    }


    private List<Integer> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.map(Integer::parseInt).collect(Collectors.toList());
        }
    }
}
