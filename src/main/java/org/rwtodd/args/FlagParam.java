package org.rwtodd.args;

/**
 * Represents a boolean flag, which takes no arguments, turns true when
 * provided.
 *
 * @author rwtodd
 */
public class FlagParam extends BasicNoArgParam<Boolean> {

  public FlagParam(Iterable<String> names, String help) {
    super(names, Boolean.FALSE, help);
  }

  @Override
  public void process(String param) throws ArgParserException {
    this.arg = Boolean.TRUE;
  }
}
