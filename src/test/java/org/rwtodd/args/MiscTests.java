package org.rwtodd.args;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MiscTests {

    @ParameterizedTest
    @CsvSource(textBlock = """
            -v, 1
            -vv, 2
            -vvv, 3
            -vvvv, 4
            -vvvvv, 5
            """)
    void testCountingArg(String s, int c) throws ArgParserException {
        final var cp = new CountingParam(List.of("v"), "verbosity level");
        final var p = new Parser(cp);
        final var extras = p.parse(new String[] { "subcommand", s }, 1);
        assertEquals(c, cp.getValue());
        assertEquals(0,extras.size());
    }

    @Test
    void testDefaultValues() throws ArgParserException {
        final var ip = new IntParam(List.of("ip"), 10, "help");
        final var dp = new DoubleParam(List.of("dp"), 10.0, "help");
        final var cp = new CharParam(List.of("cp"), '1', "help");
        final var sp = new StringParam(List.of("sp"), null, "help");
        final var parser = new Parser(ip,cp,dp,sp);
        final var extras = parser.parse(new String[] {"one", "two", "three"});
        assertEquals(3, extras.size());
        assertEquals(10,ip.getValue());
        assertEquals(10.0, dp.getValue(), 0.000001);
        assertEquals('1', cp.getValue());
        assertNull(sp.getValue());
    }

    @Test
    void testParsedValues() throws ArgParserException {
        final var ip = new IntParam(List.of("ip"), 10, "help");
        final var dp = new DoubleParam(List.of("dp"), 10.0, "help");
        final var cp = new CharParam(List.of("cp"), '1', "help");
        final var sp = new StringParam(List.of("s"), null, "help");
        final var parser = new Parser(ip,cp,dp,sp);
        final var extras = parser.parse(new String[] {"--ip=100","--dp", "45.45", "--cp=X", "-s" ,"--", "--hello"});
        assertEquals(0, extras.size());
        assertEquals(100,ip.getValue());
        assertEquals(45.45, dp.getValue(), 0.000001);
        assertEquals('X', cp.getValue());
        assertEquals("--hello", sp.getValue());
    }

    @Test
    void testEmptyEqualsArg() throws ArgParserException {
        final var sp = new StringParam(List.of("sp"), null, "help");
        final var parser = new Parser(sp);
        final var extras = parser.parse(new String[] {"--sp=" ,"--", "--hello"});
        assertEquals(1, extras.size());
        assertEquals("",sp.getValue());
    }

    @Test
    void testLooksLikeArg() throws ArgParserException {
        final var ip = new IntParam(List.of("ip"), 10, "help");
        final var dp = new DoubleParam(List.of("dp"), 10.0, "help");
        final var cp = new CharParam(List.of("cp"), '1', "help");
        final var sp = new StringParam(List.of("s"), null, "help");
        final var parser = new Parser(ip,cp,dp,sp);
        final var extras = parser.parse(new String[] {"--", "--ip=100","--dp", "45.45", "--cp=X", "-s" ,"--", "--hello"});
        assertEquals(7, extras.size());
        assertEquals(10, ip.getValue());
        assertEquals(10.0, dp.getValue(), 0.00001);
        assertEquals('1', cp.getValue());
        assertNull(sp.getValue());
    }

    @Test
    void testThatDashIsOK() throws ArgParserException {
        final var sp = new StringParam(List.of("fn"), null, "help");
        final var parser = new Parser(sp);
        final var extras = parser.parse(new String[] {"--fn", "-"});
        assertEquals(0, extras.size());
        assertEquals("-", sp.getValue());
    }

    @ParameterizedTest
    @CsvSource(delimiterString = "|", textBlock = """
            1,2,3     |  1,2,3
            1,5..8,21 | 1,5,6,7,8,21
            1..10     | 1,2,3,4,5,6,7,8,9,10
            5,4,1     | 5,4,1
            """)
    void testIntListParam(String inS, String outS) throws ArgParserException {
        final var answer = Arrays.stream(outS.split(",")).mapToInt(Integer::parseInt).toArray();
        final var ilp = new IntListParam(List.of("il"), "help");
        final var parser = new Parser(ilp);
        final var extras = parser.parse(new String[] {"--il", inS});
        assertEquals(0, extras.size());
        assertArrayEquals(answer, ilp.getValue().toArray());
    }

    @Test
    void testBadDoubleArg() throws ArgParserException {
        final var sp = new DoubleParam(List.of("flt"), null, "help");
        final var parser = new Parser(sp);
        assertThrows(ArgParserException.class, () -> {
            final var extras = parser.parse(new String[] {"--flt", "-"});
        });
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            today, 0
            yesterday, -1
            tomorrow, 1
            t+40, 40
            t-10, -10
            """)
    void testAdvancedLocalDate(String darg, int delta) throws ArgParserException {
        final var ldp = new DateParam(List.of("date"), "help");
        final var today = LocalDate.now();
        final var parser = new Parser(ldp);
        final var extras = parser.parse(new String[] {"--date", darg});
        assertEquals(0,extras.size());
        assertEquals(today.plusDays(delta), ldp.getValue());
    }

    @Test
    void testAbbrevLocalDate() throws ArgParserException {
        final var ldp = new DateParam(List.of("date"), "help");
        final var today = LocalDate.now();
        final var parser = new Parser(ldp);
        final var extras = parser.parse(new String[] {"--date", "7-12"});
        assertEquals(0,extras.size());
        assertEquals(LocalDate.of(today.getYear(),7,12), ldp.getValue());
    }

    @Test
    void testAppendingParam() throws ArgParserException {
        final var ap = new AppendingParam<>(new IntParam(List.of("p"),1,"helo text"));
        final var p = new Parser(ap);
        final var extras = p.parse(new String[] { "subcommand", "-p","21", "-p", "500", "-p", "1200"}, 1);
        assertEquals(0, extras.size());
        assertIterableEquals(List.of(21,500,1200), ap.getValue());
    }
}
