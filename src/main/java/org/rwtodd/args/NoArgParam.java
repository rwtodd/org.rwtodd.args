package org.rwtodd.args;

/**
 * An interface for a parameter that doesn't take arguments.  Typically
 * these are simple boolean flags (see {@link FlagParam} for a typical flag).
 */
public non-sealed interface NoArgParam<T> extends Param {
  /**
   * Process a parameter found on the command-line.
   * @param param the name of the parameter.
   * @throws ArgParserException when there is a problem processing the parameter.
   */
  void process(String param) throws ArgParserException;

  /** gets the current value of the parameter */
  T getValue();
}
