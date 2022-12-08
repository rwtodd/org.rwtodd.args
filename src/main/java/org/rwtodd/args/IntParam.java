package org.rwtodd.args;

/**
 * {@code IntParam} is a Param that accepts integer arguments.
 *
 * @author Richard Todd
 */
public class IntParam extends BasicOneArgParam<Integer> {

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public IntParam(Iterable<String> names, Integer dflt, String help) {
    super(names, dflt, help);
  }

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
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
