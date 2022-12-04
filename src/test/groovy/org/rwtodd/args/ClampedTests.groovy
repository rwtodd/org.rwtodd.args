package org.rwtodd.args

import spock.lang.Specification

import java.time.LocalDate

class ClampedTests extends Specification {

    // helper method to compare doubles with epsilon
    static boolean sameAs(Double a, Double b) {
        a == b || (Math.abs(a-b) < 0.0000001)
    }

    def "test clamped integer in range"() {
        given:
        final var cip = new ClampedParam(new IntParam(['seven-bit'], 0, "Give a 7-bit positive number"), 0, 127);
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--seven-bit=$n"}, 0) == []
        cip.value == n
        where:
        n << [0,1,10,126,127]
    }

    def "test clamped integer out of range"() {
        given:
        final var cip = new ClampedParam(new IntParam(['seven-bit'], 0,  "Give a 7-bit positive number"), 0, 127,);
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--seven-bit=$n"}, 0) == []
        cip.value == cn
        where:
        n | cn
        128 | 127
        130 | 127
        2000 | 127
        -100 | 0
        -1 | 0
    }

    def "test clamped double in range"() {
        given:
        final var cip = new ClampedParam(new DoubleParam(['unit'], 0, "Give a number [0,1]"), 0.0d, 1.0d);
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--unit=$n"}, 0) == []
        sameAs(cip.value,n)
        where:
        n << [0.0d,0.1d,0.25d,0.126d,0.999d]
    }

    def "test clamped double out of range"() {
        given:
        final var cip = new ClampedParam(new DoubleParam(['unit'], 0, "Give a number [0,1]"), 0.0d, 1.0d);
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--unit=$n"}, 0) == []
        sameAs(cip.value,cn)
        where:
        n | cn
        128 | 1.0d
        1.1 | 1.0d
        1.001 | 1.0d
        -100 | 0.0d
        -0.001 | 0.0d
    }

    def "test clamped string"() {
        given:
        final var cip = new ClampedParam(new StringParam(['name'], "Alpha", "Give a name starting with A"), 'A', 'Azzzzzz');
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--name=$n"}, 0) == []
        cip.value == cn
        where:
        n           | cn
        '0-ply'     | 'A'
        'Armistice' | 'Armistice'
        'very'      | 'Azzzzzz'
        'Bavarian'  | 'Azzzzzz'
    }

    def "test clamped date in range"() {
        given:
        final var cip = new ClampedParam(new DateParam(['day'], "Give a day"), LocalDate.of(2020,1,1), LocalDate.of(2032,12,31));
        final var p = new Parser(cip)
        expect:
        p.parse(new String[] {"--day=$n"}, 0) == []
        cip.value == cn
        where:
        n            | cn
        "2022-1-11"  | LocalDate.of(2022,1,11)
        "2019-12-31" | LocalDate.of(2020,1,1)
        "2037-01-01" | LocalDate.of(2032,12,31)
    }
}