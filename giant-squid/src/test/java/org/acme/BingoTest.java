package org.acme;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class BingoTest {
    final Bingo underTest = new Bingo();

    public static final String PATH = "src/test/resources/largeInput.txt";
    public static final String PATH_TO_SMALL = "src/test/resources/smallInput.txt";

    @Test
    public void shouldSolveSmallPuzzle() throws IOException {
        underTest.play(readFromFile(PATH_TO_SMALL));
        assertThat(underTest.getScore()).isEqualTo(4512);
    }

    @Test
    public void shouldLooseSmallPuzzle() throws IOException {
        underTest.play2Loose(readFromFile(PATH_TO_SMALL));
        assertThat(underTest.getScore()).isEqualTo(1924);
    }

    @Test
    public void shouldLooseBigPuzzle() throws IOException {
        underTest.play2Loose(readFromFile(PATH));
        assertThat(underTest.getScore()).isEqualTo(31755);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        underTest.play(readFromFile(PATH));
        assertThat(underTest.getScore()).isEqualTo(6592);
    }

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }
}