package org.rwtodd.args;

import java.util.List;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SetRestrictedTests {
    @ParameterizedTest
    @ValueSource(chars = {'a','r','c','h','e','s'})
    void testRestrictedCharSet1(char n) throws ArgParserException {
        final var rp = new SetRestrictedParam<>(new CharParam(List.of("unit"),'c', "give a letter from <arches>"),
                Set.of('a','r','c','h','e','s'));
        final var p = new Parser(rp);
        var extras = p.parse(new String[]{"--unit="+n});
        assertEquals(0,extras.size());
        assertEquals(n,rp.getValue());
    }

    @ParameterizedTest
    @ValueSource(chars = {'v','z','1','~','$'})
    void testRestrictedCharSet2(char n) throws ArgParserException {
        final var rp = new SetRestrictedParam<>(new CharParam(List.of("unit"),'c', "give a letter from <arches>"),
                Set.of('a','r','c','h','e','s'));
        final var p = new Parser(rp);
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[]{"--unit="+n});
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10,15,25,30,35,40,45})
    void testRestrictedIntSet1(int n) throws ArgParserException {
        final var rp = new SetRestrictedParam<>(new IntParam(List.of("unit"),5, "give a low multiple of 5"),
                IntStream.range(1,10).mapToObj(x -> x*5).toList());
        final var p = new Parser(rp);
        var extras = p.parse(new String[]{"--unit="+n});
        assertEquals(0,extras.size());
        assertEquals(n,rp.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,6,7,8,9})
    void testRestrictedIntSet2(int n) throws ArgParserException {
        final var rp = new SetRestrictedParam<>(new IntParam(List.of("unit"),5, "give a letter from <arches>"),
                IntStream.range(1,10).mapToObj(x -> x*5).toList());
        final var p = new Parser(rp);
        assertThrows(ArgParserException.class, () -> {
            var extras = p.parse(new String[]{"--unit="+n});
        });
    }

}
