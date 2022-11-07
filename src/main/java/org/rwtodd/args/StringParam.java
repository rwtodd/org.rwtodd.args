package org.rwtodd.args;

import java.util.Collection;

/**
 *
 * @author rwtodd
 */
public class StringParam extends BasicOneArgParam<String> {
  public StringParam(Collection<String> names, String dflt, String help) {
    super(names, dflt, help);
  }
    
  public StringParam(Collection<String> names, String help) {
    super(names, null, help);
  }

  @Override
  protected String convertArg(String param, String argument) throws Exception {
    return argument;
  }
}
