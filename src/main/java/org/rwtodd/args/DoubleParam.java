package org.rwtodd.args;

import java.util.Collection;

/**
 * DoubleParam is a Param that accepts double arguments.
 *
 * It defaults to NaN if another default is not selected.
 * @author rwtodd
 */
public class DoubleParam extends BasicOneArgParam<Double> {

  public DoubleParam(Collection<String> names, Double dflt, String help) {
    super(names, dflt, help);
  }
    
  public DoubleParam(Collection<String> names, String help) {
    super(names, Double.NaN, help);
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