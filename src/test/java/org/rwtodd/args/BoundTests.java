package org.rwtodd.args;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BoundTests {
  @ParameterizedTest
  @ValueSource(ints = {0,1,10,126,127})
  void testBoundedIntegerInRange(int n) throws ArgParserException {
     final var bip = new BoundedParam<>(new IntParam(List.of("seven-bit"), 0, "Give a 7-bit positive number"), 0, 127);
     final var p = new Parser(bip);
     var extras = p.parse(new String[] {"--seven-bit=" + n}, 0);
     assertEquals(0, extras.size());
     assertEquals(n, bip.getValue());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1,128,1138})
  void testBoundedIntegerOutOfRange(final int n) throws ArgParserException {
     final var bip = new BoundedParam<>(new IntParam(List.of("seven-bit"), 0, "Give a 7-bit positive number"), 0, 127);
     final var p = new Parser(bip);
     assertThrows(ArgParserException.class, () -> {
         var extras = p.parse(new String[] {"--seven-bit=" + n}, 0);
     });
  }

    @ParameterizedTest
    @ValueSource(ints = {0,1,10,126})
    void testBoundedIntegerInRangeExclusive(int n) throws ArgParserException {
        final var bip = new BoundedParam<>(new IntParam(List.of("seven-bit"), 0, "Give a 7-bit positive number"), 0, 127, BoundedParam.BoundType.Exclusive);
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--seven-bit=" + n}, 0);
        assertEquals(0, extras.size());
        assertEquals(n, bip.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,127,1138})
    void testBoundedIntegerOutOfRangeExclusive(final int n) throws ArgParserException {
        final var bip = new BoundedParam<>(new IntParam(List.of("seven-bit"), 0, "Give a 7-bit positive number"), 0, 127, BoundedParam.BoundType.Exclusive);
        final var p = new Parser(bip);
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[] {"--seven-bit=" + n}, 0);
        });
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 0.6992, 0.999})
    void testBoundedDoubleInRange(double n) throws ArgParserException {
        final var bip = new BoundedParam<>(new DoubleParam(List.of("unit"), 0.0, "Give a unit [0,1] number"), 0.0, 1.0);
        final var p = new Parser(bip);
        var extras = p.parse(new String[] {"--unit=" + n}, 0);
        assertEquals(0, extras.size());

        assertEquals(n, bip.getValue(), 0.00001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0001,-0.00001,-0.5,20.32})
    void testBoundedDoubleOutOfRange(double n) throws ArgParserException {
        final var bip = new BoundedParam<>(new DoubleParam(List.of("unit"), 0.0, "Give a unit [0,1] number"), 0.0, 1.0);
        final var p = new Parser(bip);
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[] {"--unit=" + n}, 0);
        });
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, -0.00001,-0.5,20.32})
    void testBoundedDoubleOutOfRangeExclusive(double n) throws ArgParserException {
        final var bip = new BoundedParam<>(new DoubleParam(List.of("unit"), 0.0, "Give a unit [0,1] number"), 0.0, 1.0, BoundedParam.BoundType.Exclusive);
        final var p = new Parser(bip);
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[] {"--unit=" + n}, 0);
        });
    }
}
