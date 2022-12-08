package org.rwtodd.args;

/**
 * Represents a boolean flag, which takes no arguments, turns true when
 * provided.
 *
 * @author Richard Todd
 */
public class FlagParam extends BasicNoArgParam<Boolean> {

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
  public FlagParam(Iterable<String> names, String help) {
    super(names, Boolean.FALSE, help);
  }

  @Override
  public void process(String param) throws ArgParserException {
    this.arg = Boolean.TRUE;
  }
}
