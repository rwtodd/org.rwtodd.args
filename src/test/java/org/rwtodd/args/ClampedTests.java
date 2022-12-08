package org.rwtodd.args;

import java.util.List;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ClampedTests {
    @ParameterizedTest
    @CsvSource(textBlock = """
            -10, 0
            0, 0
            5, 5
            100, 100
            127, 127
            128, 127
            1000, 127
            """)
    void testClampedInteger(int n, int cn) throws ArgParserException {
        final var bip = new ClampedParam<>(new IntParam(List.of("seven-bit"), 0, "Give a 7-bit positive number"), 0, 127);
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--seven-bit=" + n}, 0);
        assertEquals(0, extras.size());
        assertEquals(cn, bip.getValue());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            -10.0, 0.0
            -0.001, 0.0
            0.0, 0.0
            0.5, 0.5
            1.00001, 1.0
            127.234, 1.0
            128, 1.0
            """)
    void testClampedDouble(double n, double cn) throws ArgParserException {
        final var bip = new ClampedParam<>(new DoubleParam(List.of("unit"), 0.0, "in unit [0,1]"), 0.0, 1.0);
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--unit=" + n}, 0);
        assertEquals(0, extras.size());
        assertEquals(cn, bip.getValue(), 0.00001);
    }

    @ParameterizedTest
    @CsvSource(textBlock= """
            0-ply, A
            Armistice, Armistice
            very, Azzzzzz
            Bavarian, Azzzzzz
            """)
    void testClampedString(String s, String cs) throws ArgParserException {
        final var bip = new ClampedParam<>(new StringParam(List.of("name"), "Alpha", "Give a name starting with A"), "A", "Azzzzzz");
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--name=" + s}, 0);
        assertEquals(0, extras.size());
        assertEquals(cs, bip.getValue());
    }

    @ParameterizedTest
    @CsvSource(textBlock= """
            2022-1-11 , 2022-01-11
            2019-12-31, 2020-01-01
            """)
    void testClampedString(String d, LocalDate cd) throws ArgParserException {
        final var bip = new ClampedParam<>(new DateParam(List.of("day"), "Give a day"), LocalDate.of(2020,1,1), LocalDate.of(2032,12,31));
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--day=" + d}, 0);
        assertEquals(0, extras.size());
        assertEquals(cd, bip.getValue());
    }

}
