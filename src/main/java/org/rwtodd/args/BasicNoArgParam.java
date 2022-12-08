package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

/**
 * A base class which should work for most named parameters which
 * take zero arguments.  It handles storing the value and providing
 * names and help text to the {@link Parser}.
 *
 * @param <T> The type of value that this parameter constructs
 */
public abstract class BasicNoArgParam<T> implements NoArgParam<T> {
  /** the value of the argument to this parameter */
  protected T arg;

  /** the names by which this parameter may be invoked on the command-line */
  protected final Iterable<String> paramNames;

  /**
   * The help text to be displayed for this parameter.
   */
  protected final String helpText;

  /**
   * Construct a basic parameter which takes no arguments.
   * @param names the names by which this parameter is invoked.
   * @param dflt the default value of the parameter if it isn't invoked.
   * @param help the help text for the parameter.
   */
  public BasicNoArgParam(Iterable<String> names, T dflt, String help) {
    arg = dflt;
    paramNames = names;
    helpText = help; 
  }

  @Override public T getValue() { return arg; }

  @Override
  public void addToMap(Map<String,Param> map) {
    for(String s: paramNames) {
      map.put(s,this);
    }
  }

  @Override
  public void addHelp(PrintStream ps) {
    Param.formatTypicalHelp(ps, Param.formatNames(paramNames), helpText);
  }
}
