package org.rwtodd.args;

/**
 * An interface for parameters that take an argument from the command line.
 *
 * @author rwtodd
 */
public non-sealed interface OneArgParam extends Param {
  /**
   * Process an parameter with its argument.
   * @param param the name of the parameter, as it was found in the command-line.
   * @param argument the given argument to the parameter.
   * @throws ArgParserException if there is a problem parsing the argument.
   */
  void process(String param, String argument) throws ArgParserException;
}
