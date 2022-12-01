package org.rwtodd.args;


/**
 *
 * @author rwtodd
 */
public class StringParam extends BasicOneArgParam<String> {
  public StringParam(Iterable<String> names, String dflt, String help) {
    super(names, dflt, help);
  }
    
  public StringParam(Iterable<String> names, String help) {
    this(names, null, help);
  }

  @Override
  protected String convertArg(String param, String argument) throws Exception {
    return argument;
  }
}
