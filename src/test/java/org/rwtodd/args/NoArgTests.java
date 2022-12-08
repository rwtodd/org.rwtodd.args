package org.rwtodd.args;

import java.util.List;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class NoArgTests {

    @Test
    void testFlagNotGiven() throws ArgParserException {
        final var fp = new FlagParam(List.of("order","o"), "flag param");
        final var p = new Parser(fp);
        var extras = p.parse(new String[] {"ho", "ho"}, 0);
        assertEquals(2, extras.size());
        assertFalse(fp.getValue());
    }

    @Test
    void testFlagGiven() throws ArgParserException {
        final var fp = new FlagParam(List.of("order","o"), "flag param");
        final var p = new Parser(fp);
        var extras = p.parse(new String[] {"-o", "ho", "ho"}, 0);
        assertEquals(2, extras.size());
        assertTrue(fp.getValue());
    }

    @Test
    void testSplittingCombinedFlags() throws ArgParserException {
        final var fp = new FlagParam(List.of("order","o"), "flag param");
        final var cp = new CountingParam(List.of("v"), "verbosity level");
        final var p = new Parser(fp,cp);
        var extras = p.parse(new String[] {"-vovv", "ho", "ho"}, 0);
        assertEquals(2, extras.size());
        assertTrue(fp.getValue());
        assertEquals(3, cp.getValue());
    }

    @Test
    void testSplittingCombinedWithArg() throws ArgParserException {
        final var fp = new FlagParam(List.of("order","o"), "flag param");
        final var cp = new CountingParam(List.of("v"), "verbosity level");
        final var argp = new StringParam(List.of("file","f"), "a file");
        final var p = new Parser(fp,cp, argp);
        var extras = p.parse(new String[] {"-vovvf", "ho.txt", "ho"}, 0);
        assertEquals(1, extras.size());
        assertTrue(fp.getValue());
        assertEquals(3, cp.getValue());
        assertEquals("ho.txt", argp.getValue());
    }

    @Test
    void testSplittingCombinedMissingArg() throws ArgParserException {
        final var fp = new FlagParam(List.of("order","o"), "flag param");
        final var cp = new CountingParam(List.of("v"), "verbosity level");
        final var argp = new StringParam(List.of("file","f"), "a file");
        final var p = new Parser(fp,cp, argp);

        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[] {"-vofv", "ho.txt", "ho"}, 0);
        });
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[] {"-vof"}, 0);
        });
    }

    private enum TestEnum { rocket, socket }
    @Test
    void testEnumParam() throws ArgParserException {
        final var ep = new EnumParam<>(TestEnum.class, "Give one of the values");
        final var p = new Parser(ep);
        var extras = p.parse(new String[] {"--rocket"});
        assertEquals(0, extras.size());
        assertEquals(TestEnum.rocket, ep.getValue());
        extras = p.parse(new String[] {"--socket"});
        assertEquals(0, extras.size());
        assertEquals(TestEnum.socket, ep.getValue());
    }

}
