package org.rwtodd.args;


/**
 * A parameter that just stores a string from the command-line.
 *
 * @author Richard Todd
 */
public class StringParam extends BasicOneArgParam<String> {
  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public StringParam(Iterable<String> names, String dflt, String help) {
    super(names, dflt, help);
  }

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
  public StringParam(Iterable<String> names, String help) {
    this(names, null, help);
  }

  @Override
  protected String convertArg(String param, String argument) throws Exception {
    return argument;
  }
}
