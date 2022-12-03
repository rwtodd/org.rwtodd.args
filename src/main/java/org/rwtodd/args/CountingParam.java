package org.rwtodd.args;

/**
 * A Param that counts the number of times it is seen.  It starts counting from the
 * default value, which is 0 if not provided.
 *
 * @author rwtodd
 */
public class CountingParam extends BasicNoArgParam<Integer> {

  public CountingParam(Iterable<String> names, int dflt, String help) {
    super(names, dflt, help);
  }

  public CountingParam(Iterable<String> names, String help) {
    this(names, 0, help);
  }

  @Override
  public void process(String param) throws ArgParserException {
    this.arg += 1;
  }

}
