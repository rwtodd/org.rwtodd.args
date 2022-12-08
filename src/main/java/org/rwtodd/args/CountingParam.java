package org.rwtodd.args;

/**
 * A Param that counts the number of times it is seen.  It starts counting from the
 * default value, which is 0 if not provided.
 *
 * @author Richard Todd
 */
public class CountingParam extends BasicNoArgParam<Integer> {

  /**
   * Constructs the parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public CountingParam(Iterable<String> names, int dflt, String help) {
    super(names, dflt, help);
  }

  /**
   * Constructs the parameter with a default of 0.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param help the help string for this parameter.
   */
  public CountingParam(Iterable<String> names, String help) {
    this(names, 0, help);
  }

  @Override
  public void process(String param) throws ArgParserException {
    this.arg += 1;
  }

}
