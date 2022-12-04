package org.rwtodd.args

import spock.lang.Specification

class NoArgTests extends Specification {

    def "test flag not given"() {
        given:
        final var fp = new FlagParam(['orders','o'], "send orders?")
        final var p = new Parser(fp)
        when:
        var extras = p.parse('ho', 'ho')
        then:
        extras.size() == 2
        !fp.value
    }

    def "test flag is given"() {
        given:
        final var fp = new FlagParam(['orders','o'], "send orders?")
        final var p = new Parser(fp)
        when:
        var extras = p.parse('-o', 'ho')
        then:
        extras.size() == 1
        fp.value
    }

    def "test splitting combined small flags"() {
        given:
        final var ord = new FlagParam(['orders','o'], "send orders?")
        final var verbose = new CountingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
        final var p = new Parser(ord,verbose)
        when:
        var extras = p.parse('-vovv', 'ho')
        then:
        extras.size() == 1
        ord.value
        verbose.value == 3
    }

    def "test splitting combined small flags with arg"() {
        given:
        final var ord = new FlagParam(['orders','o'], "send orders?")
        final var verbose = new CountingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
        final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
        final var p = new Parser(ord,verbose,starg)
        when:
        var extras = p.parse('-vof', 'market.txt')
        then:
        extras.size() == 0
        ord.value
        verbose.value == 1
        starg.value == 'market.txt'
    }

    def "test small flags with missing arg"() {
        given:
        final var ord = new FlagParam(['orders','o'], "send orders?")
        final var verbose = new CountingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
        final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
        final var p = new Parser(ord,verbose,starg)
        when:
        var extras = p.parse(new String[] {'-vof'}, 0)
        then:
        ArgParserException e = thrown()
    }

    def "test small flags with out-of-order arg"() {
        given:
        final var ord = new FlagParam(['orders','o'], "send orders?")
        final var verbose = new CountingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
        final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
        final var p = new Parser(ord,verbose,starg)
        when:
        var extras = p.parse('-vfo', 'market.txt')
        then:
        ArgParserException e = thrown()
    }

    private enum TestEnum { rocket, socket }
    def "test enum params"() {
        given:
        final var ep = new EnumParam<>(TestEnum.class, "Give one of the enum values")
        final var p = new Parser(ep)
        expect:
        p.parse(new String[] {n},0) == []
        ep.value == v
        where:
        n          | v
        '--rocket' | TestEnum.rocket
        '--socket' | TestEnum.socket
    }

}
