package org.rwtodd.args


import spock.lang.Specification

class BoundTests extends Specification {

    // helper method to compare doubles with epsilon
    static boolean sameAs(Double a, Double b) {
        a == b || (Math.abs(a-b) < 0.0000001)
    }


    def "test bounded integer in range"() {
        given:
        final var bip = new BoundedParam(new IntParam(['seven-bit'], 0, "Give a 7-bit positive number"), 0, 127)
        final var p = new Parser(bip)
        expect:
        p.parse(new String[] {"--seven-bit=$n"}, 0) == []
        bip.value == n
        where:
        n << [0,1,10,126,127]
    }

    def "test bounded integer out of range"() {
        given:
        final var bip = new BoundedParam(new IntParam(['seven-bit'], 0, "Give a 7-bit positive number"), 0, 127)
        final var p = new Parser(bip)
        when:
        var extras = p.parse(new String[] { "--seven-bit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << ["128", "2038", "-1"]
    }

    def "test bounded integer out of range exclusive"() {
        given:
        final var bip = new BoundedParam(new IntParam(['seven-bit'], 0, "Give a 7-bit positive number"), 0, 127, BoundedParam.BoundType.Exclusive)
        final var p = new Parser(bip)
        when:
        var extras = p.parse(new String[] { "--seven-bit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << ["127", "208", "-1", "-102"]
    }


    def "test bounded double in range"() {
        given:
        final var bip = new BoundedParam(new DoubleParam(['unit'], 0.0d, "Give a unit [0,1] number"), 0.0d, 1.0d)
        final var p = new Parser(bip)
        expect:
        p.parse(new String[] {"--unit=$n"}, 0) == []
        sameAs(bip.value,n)
        where:
        n << [0.0d,0.1d,0.6993d,0.999d]
    }

    def "test bounded double out of range"() {
        given:
        final var bip = new BoundedParam(new DoubleParam(['unit'], 0.0d, "Give a unit [0,1] number"), 0.0d, 1.0d)
        final var p = new Parser(bip)
        when:
        var extras = p.parse(new String[] { "--unit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << ["1.1", "3.1", "-0.001"]
    }

    def "test bounded double out of range exclusive"() {
        given:
        final var bip = new BoundedParam(new DoubleParam(['unit'], 0.0d, "Give a unit [0,1) number"), 0.0d, 1.0d, BoundedParam.BoundType.Exclusive)
        final var p = new Parser(bip)
        when:
        var extras = p.parse(new String[] { "--unit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << ["1.0","2.0","-0.0001"]
    }

}
