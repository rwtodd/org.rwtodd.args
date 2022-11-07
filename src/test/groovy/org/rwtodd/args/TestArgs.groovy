package org.rwtodd.args

import spock.lang.Specification

class BasicTests extends Specification {

  def "test accumulator"() {
    given:
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
      final var p = new Parser(verbose)
    when:
      var extras = p.parse('-vvvv', '-v')
    then:
      extras.empty
      verbose.value == 5
  }

  def "test int param 1"() {
    given:
      final var ip = new IntParam(['count'], "<N> The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse("file", "file");
    then:
      extras.size() == 2
      ip.value == Integer.MIN_VALUE
  }

  def "test int param 2"() {
    given:
      final var ip = new IntParam(['count'], "<N> The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse('--count=21');
    then:
      extras.size() == 0
      ip.value == 21
  }

  def "test int param 3"() {
    given:
      final var ip = new IntParam(['count'], "<N> The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse('--count', '486');
    then:
      extras.size() == 0
      ip.value == 486 
  }

  def "test string param 1"() {
    given:
      final var sp = new StringParam(['name'], "<NAME> The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('file', 'file')
    then:
      extras.size() == 2
      sp.value == null
  }  

  def "test string param 2"() {
    given:
      final var sp = new StringParam(['name'], '<NAME> The name you want.')
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name=wally');
    then:
      extras.size() == 0
      sp.value == 'wally'
  }  

  def "test string param 3"() {
    given:
      final var sp = new StringParam(['name'], "<NAME> The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name=', 'file');
    then:
      extras.size() == 1
      sp.value == ''
  }  

  def "test string param 4"() {
    given:
      final var sp = new StringParam(['name'], "<NAME> The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name', 'larry');
    then:
      extras.size() == 0
      sp.value == 'larry'
  }  

  def "test short expansion missing arg"() {
    given:
      final var p = new Parser(new IntParam(['orders','o'], 5, "<COUNT> how many?"))
    when:
      var extras = p.parse('-o')
    then:
      ArgParserException e = thrown()
  }

  def "test short expansion with argument"() {
    given:
      final var ip = new IntParam(['orders','o'], 5, "<COUNT> how many?")
      final var p = new Parser(ip)
    when:
      var extras = p.parse('-o', '2')
    then:
      extras.empty
      ip.value == 2
  }

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
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
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
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
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
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
      final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
      final var p = new Parser(ord,verbose,starg)
    when:
      var extras = p.parse('-vof')
    then:
      ArgParserException e = thrown()
  }

  def "test small flags with out-of-order arg"() {
    given:
      final var ord = new FlagParam(['orders','o'], "send orders?")
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
      final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
      final var p = new Parser(ord,verbose,starg)
    when:
      var extras = p.parse('-vfo', 'market.txt')
    then:
      ArgParserException e = thrown()
  }

  def "test of -- verbatim args 1"() {
    given:
      final var ord = new FlagParam(['orders','o'], "send orders?")
      final var p = new Parser(ord)
    when:
      var extras = p.parse('--', '-vof', '--', '--market.txt')
    then:
      !ord.value
      extras == ['-vof', '--', '--market.txt']
  }

  def "test of -- verbatim args 2"() {
    given:
      final var ord = new FlagParam(['orders','o'], "send orders?")
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
      final var starg = new StringParam(['fname','f'], '<FNAME> the File name')
      final var p = new Parser(ord,verbose,starg)
    when:
      var extras = p.parse('-vof', '--', '--market.txt')
    then:
      extras.size() == 0
      ord.value
      verbose.value == 1 
      starg.value == '--market.txt'
  }

  def "test that - is ok as an argument"() {
    given:
      final var sp = new StringParam(['fname'],'<FNAME> the file to use')
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--fname','-') 
    then:
      extras.size() == 0       
      sp.value == '-'
  }
}
 
