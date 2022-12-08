package org.rwtodd.args;

/**
 * CharParam is a Param that accepts single-character arguments.
 *
 * It defaults to ' ' (space) if another default is not selected.
 * @author Richard Todd
 */
public class CharParam extends BasicOneArgParam<Character> {

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public CharParam(Iterable<String> names, Character dflt, String help) {
    super(names, dflt, help);
  }

  /**
   * Constructs the parameter to default to a space if the parameter does not appear on the command-line.
   *
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
  public CharParam(Iterable<String> names, String help) {
    this(names, ' ', help);
  }

  @Override
  protected Character convertArg(String param, String arg) throws ArgParserException {
    if(arg.length() != 1)
      throw new ArgParserException(String.format("Argument for <%s> is not a single character!", param));
    return arg.charAt(0);
  }

}
