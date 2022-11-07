package org.rwtodd.args;
import java.util.Collection;

/**
 * Represents a boolean flag, which takes no arguments, turns true when
 * provided.
 *
 * @author rwtodd
 */
public class FlagParam extends BasicNoArgParam<Boolean> {

  public FlagParam(Collection<String> names, String help) {
    super(names, Boolean.FALSE, help);
  }

  @Override
  public void process(String param) throws ArgParserException {
    this.arg = Boolean.TRUE;
  }
}
