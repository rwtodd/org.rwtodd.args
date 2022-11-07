package org.rwtodd.args

import spock.lang.Specification

class BasicTests extends Specification {

  def "test accumulator"() {
    given:
      final var verbose = new AccumulatingParam("verbose", 'v' as char, "Be more verbose (can repeat this arg)")
      final var p = new Parser(verbose, new HelpParam())
    when:
      var extras = p.parse("-vvvv", "-v")
    then:
      extras.empty
      verbose.value == 5
  }

  def "test int param 1"() {
    given:
      final var ip = new IntParam("count", ' ' as char, "n", "The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse("file", "file");
    then:
      extras.size() == 2
      ip.value == Integer.MIN_VALUE
  }

  def "test int param 2"() {
    given:
      final var ip = new IntParam("count", ' ' as char, "n", "The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse('--count=21');
    then:
      extras.size() == 0
      ip.value == 21
  }

  def "test int param 3"() {
    given:
      final var ip = new IntParam("count", ' ' as char, "n", "The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse('--count', '486');
    then:
      extras.size() == 0
      ip.value == 486 
  }

  def "test string param 1"() {
    given:
      final var sp = new StringParam("name", ' ' as char, "name", "The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('file', 'file')
    then:
      extras.size() == 2
      sp.value == ""
  }  

  def "test string param 2"() {
    given:
      final var sp = new StringParam("name", ' ' as char, "name", "The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name=wally');
    then:
      extras.size() == 0
      sp.value == 'wally'
  }  


  def "test string param 3"() {
    given:
      final var sp = new StringParam("name", ' ' as char, "name", "The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name=', 'file');
    then:
      extras.size() == 1
      sp.value == ''
  }  

  def "test string param 4"() {
    given:
      final var sp = new StringParam("name", ' ' as char, "name", "The name you want.")
      final var p = new Parser(sp)
    when:
      var extras = p.parse('--name', 'larry');
    then:
      extras.size() == 0
      sp.value == 'larry'
  }  

  def "test short expansion missing arg"() {
    given:
      final var p = new Parser(
        new IntParam("orders", 'o' as char, "count", "how many?", 5),
        new HelpParam())
    when:
      var extras = p.parse('-o')
    then:
      CommandLineException e = thrown()
  }

  def "test short expansion with argument"() {
    given:
      final var ip = new IntParam("orders", 'o' as char, "count", "how many?", 5)
      final var p = new Parser(ip)
    when:
      var extras = p.parse('-o', '2')
    then:
      extras.empty
      ip.value == 2
  }

  def "test flag not given"() {
    given:
      final var fp = new FlagParam("orders", 'o' as char, "send orders?")
      final var p = new Parser(fp)
    when:
      var extras = p.parse('ho', 'ho')
    then:
      extras.size() == 2
      !fp.value
  }

  def "test flag is given"() {
    given:
      final var fp = new FlagParam("orders", 'o' as char, "send orders?")
      final var p = new Parser(fp)
    when:
      var extras = p.parse('-o', 'ho')
    then:
      extras.size() == 1
      fp.value
  }

}
 
