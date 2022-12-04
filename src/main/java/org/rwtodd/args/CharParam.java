package org.rwtodd.args;

/**
 * CharParam is a Param that accepts single-character arguments.
 *
 * It defaults to ' ' if another default is not selected.
 * @author rwtodd
 */
public class CharParam extends BasicOneArgParam<Character> {

  public CharParam(Iterable<String> names, Character dflt, String help) {
    super(names, dflt, help);
  }

  public CharParam(Iterable<String> names, String help) {
    this(names, ' ', help);
  }

  @Override
  protected Character convertArg(String param, String arg) throws ArgParserException {
    if(param.length() != 1)
      throw new ArgParserException(String.format("Argument for <%s> is not a single character!", param));
    return arg.charAt(0);
  }

}
