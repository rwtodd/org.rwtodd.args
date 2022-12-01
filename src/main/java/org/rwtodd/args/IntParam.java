package org.rwtodd.args;

/**
 * IntParam is a Param that accepts integer arguments.
 * @author rwtodd
 */
public class IntParam extends BasicOneArgParam<Integer> {

  public IntParam(Iterable<String> names, Integer dflt, String help) {
    super(names, dflt, help);
  }
    
  public IntParam(Iterable<String> names, String help) {
    this(names, Integer.MIN_VALUE, help);
  }

  @Override
  protected Integer convertArg(String param, String arg) throws ArgParserException {
    try {
      return Integer.valueOf(arg);
    } catch(NumberFormatException nfe) {
      throw new ArgParserException(
                  String.format("Argument for <%s> is not an integer!", param),
                  nfe);
    }
  }

}
