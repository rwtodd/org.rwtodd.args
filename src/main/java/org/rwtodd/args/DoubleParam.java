package org.rwtodd.args;

/**
 * DoubleParam is a Param that accepts double arguments.
 *
 * It defaults to NaN if another default is not selected.
 * @author Richard Todd
 */
public class DoubleParam extends BasicOneArgParam<Double> {

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public DoubleParam(Iterable<String> names, Double dflt, String help) {
    super(names, dflt, help);
  }

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
  public DoubleParam(Iterable<String> names, String help) {
    this(names, Double.NaN, help);
  }

  @Override
  protected Double convertArg(String param, String arg) throws ArgParserException {
    try {
      return Double.valueOf(arg);
    } catch(NumberFormatException nfe) {
      throw new ArgParserException(
                  String.format("Argument for <%s> is not a number!", param),
                  nfe);
    }
  }

}
