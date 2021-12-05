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

public class DiagnosticReportTest {

    final List<String> INPUT = Arrays.asList("00100",
            "11110",
            "10110",
            "10111",
            "10101",
            "01111",
            "00111",
            "11100",
            "10000",
            "11001",
            "00010",
            "01010");

    public static final String PATH = "src/test/resources/largeInput.txt";

    @Test
    public void checkPowerConsumption(){
        DiagnosticReport report = new DiagnosticReport(5);
        assertThat(report.getPowerConsumption(INPUT)).isEqualTo(198);
    }

    @Test
    public void shouldSolveLargePuzzle() throws IOException {
        DiagnosticReport report = new DiagnosticReport(12);
        assertThat(report.getPowerConsumption(readFromFile(PATH))).isEqualTo(3429254);
    }

    @Test
    public void verifyLifeSupportRating(){
        DiagnosticReport report = new DiagnosticReport(5);
        assertThat(report.verifyLifeSupportRating(INPUT)).isEqualTo(230);
    }

    @Test
    public void verifyLifeSupportRatingLarge() throws IOException {
        DiagnosticReport report = new DiagnosticReport(12);
        assertThat(report.verifyLifeSupportRating(readFromFile(PATH))).isEqualTo(5410338);
    }

    private List<String> readFromFile(final String path) throws IOException {
        try (final Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.collect(Collectors.toList());
        }
    }
}