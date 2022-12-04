package org.rwtodd.args

import spock.lang.Specification

class SetRestrictedTests extends Specification {

    def "test restricted char set 1"() {
        given:
        final var rp = new SetRestrictedParam(new CharParam(['unit'], (char)'c', "Give a letter from arches"),
                'arches'.toCharArray().toList())
        final var p = new Parser(rp)
        expect:
        p.parse(new String[] {"--unit=$n"}, 0) == []
        rp.value == n
        where:
        n << 'arches'.toCharArray()
    }

    def "test restricted char set 2"() {
        given:
        final var rp = new SetRestrictedParam(new CharParam(['unit'], (char)'c', "Give a letter from arches"),
                'arches'.toCharArray().toList())
        final var p = new Parser(rp)
        when:
        var extras = p.parse(new String[] {"--unit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << 'vulz1mpqwt'.toCharArray()
    }

    def "test restricted int set 1"() {
        given:
        final var rp = new SetRestrictedParam(
                new IntParam(['unit'], 5, "Give a low multiple of 5"),
                (1..10).collect { it * 5 })
        final var p = new Parser(rp)
        when:
        var extras = p.parse(new String[] {"--unit=$n"}, 0)
        then:
        extras.empty
        rp.value == n
        where:
        n << [5,10,15,20,25,30]
    }

    def "test restricted int set 1"() {
        given:
        final var rp = new SetRestrictedParam(
                new IntParam(['unit'], 5, "Give a low multiple of 5"),
                (1..10).collect { it * 5 })
        final var p = new Parser(rp)
        when:
        var extras = p.parse(new String[] {"--unit=$n"}, 0)
        then:
        ArgParserException e = thrown()
        where:
        n << [5,10,15,20,25,30].collect { it + 1}
    }

}
