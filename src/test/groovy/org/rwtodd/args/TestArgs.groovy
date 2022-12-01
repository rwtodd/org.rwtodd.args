package org.rwtodd.args

import spock.lang.Specification
import java.time.LocalDate

class BasicTests extends Specification {

  // helper method to compare doubles with epsilon
  static boolean sameAs(Double a, Double b) {
    a == b || (Math.abs(a-b) < 0.0000001)
  }

  def "test accumulator"() {
    given:
      final var verbose = new AccumulatingParam(['verbose', 'v'], "Be more verbose (can repeat this arg)")
      final var p = new Parser(verbose)
    when:
      var extras = p.parse(new String[] {'subcommand', '-vvvv', '-v'}, 1)
    then:
      extras.empty
      verbose.value == 5
  }

  def "test int param 1"() {
    given:
      final var ip = new IntParam(['count'], "<N> The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse(new String[] {'subcommand', 'skip', "file", "file"}, 2);
    then:
      extras.size() == 2
      ip.value == Integer.MIN_VALUE
  }

  def "test int param 2"() {
    given:
      final var ip = new IntParam(['count'], "<N> The count of things.");
      final var p = new Parser(ip);
    when:
      var extras = p.parse(new String[] {'--count=21'}, 0);
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
      var extras = p.parse(new String[] { 'subcommand', '--name=', 'file'}, 1);
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
      var extras = p.parse(new String[] {'-o'}, 0)
    then:
      ArgParserException e = thrown()
  }

  def "test short expansion with argument"() {
    given:
      final var ip = new IntParam(['orders','o'], 5, "<COUNT> how many?")
      final var p = new Parser(ip)
    when:
      var extras = p.parse( '-o', '2')
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
      var extras = p.parse(new String[] {'-vof'}, 0)
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
      var extras = p.parse(new String[] { 'subcommand', '--', '-vof', '--', '--market.txt'}, 1)
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

  def "test bounded integer in range"() {
    given:
      final var bip = new BoundedIntParam(['seven-bit'], 0, 0, 127, "Give a 7-bit positive number")
      final var p = new Parser(bip)
    expect:
      p.parse(new String[] {"--seven-bit=$n"}, 0) == []
      bip.value == n
    where:
      n << [0,1,10,126,127]
  }

  def "test bounded integer out of range"() {
    given:
      final var bip = new BoundedIntParam(['seven-bit'], 0, 0, 127, "Give a 7-bit positive number")
      final var p = new Parser(bip)
    when:
      var extras = p.parse(new String[] { '--seven-bit=128'}, 0)
    then:
      ArgParserException e = thrown()
  }

  def "test clamped integer in range"() {
    given:
      final var cip = new ClampedIntParam(['seven-bit'], 0, 0, 127, "Give a 7-bit positive number")
      final var p = new Parser(cip)
    expect:
      p.parse(new String[] {"--seven-bit=$n"}, 0) == []
      cip.value == n
    where:
      n << [0,1,10,126,127]
  }

  def "test clamped integer out of range"() {
    given:
      final var cip = new ClampedIntParam(['seven-bit'], 0, 0, 127, "Give a 7-bit positive number")
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

  def "test Integer List argument"() {
    given:
      final var ilp = new IntListParam(['articles'], '<LIST> which articles to process?')
      final var p = new Parser(ilp)
    expect:
      p.parse('--articles', lst) == []
      ilp.value.boxed().toList() == nums
    where:
      lst         | nums
      '1,2,3'     | [1,2,3]
      '1,5..8,21' | [1,5,6,7,8,21]
      '1..10'     | [1,2,3,4,5,6,7,8,9,10]
      '5,4,1'     | [5,4,1]
  }

  def "test Double argument"() {
    given:
      final var dp = new DoubleParam(['flt'], "Give a floating-point number")
      final var p = new Parser(dp)
    expect:
      p.parse(new String[] { "--flt=$n" }, 0) == []
      sameAs(dp.value,n)
    where:
      n << [0.0001,123.4567,21.2121,-1204.4123,-249123.3434313]
  }

  def "exception for bad Double argument"() {
    given:
      final var dp = new DoubleParam(['flt'], "Give a floating-point number")
      final var p = new Parser(dp)
    when:
      p.parse('--flt=hi') == []
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

  def "test date params"() {
    given:
    final var ep = new DateParam(['date'], "Give a yyyy-mm-dd date")
    final Parser p = [ep]
    expect:
    p.parse(new String[] {n},0) == []
    ep.value == v
    where:
    n                   | v
    '--date=2004-03-11' | LocalDate.of(2004,3,11)
    '--date=2025-5-5'   | LocalDate.of(2025,5,5)
    '--date=7-12'       | LocalDate.of(LocalDate.now().getYear(),7,12)
  }

  def "test date advanced params"() {
    given:
    final LocalDate now = LocalDate.now()
    final var ep = new DateParam(['date'], "Give a yyyy-mm-dd date")
    final Parser p = [ep]
    expect:
    p.parse(new String[] {n},0) == []
    ep.value == now.plusDays(v)
    where:
    n                   | v
    '--date=today'      | 0
    '--date=yesterday'  | -1
    '--date=tomorrow'   | 1
    '--date=t+40'       | 40
    '--date=t-10'       | -10
  }
}
 
