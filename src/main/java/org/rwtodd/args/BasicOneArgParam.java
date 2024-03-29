package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

/**
 * A base class for a typical, named, 1-argument parameter.  It is expected
 * that many custom parameters can be built as one-off anonymous classes
 * with this class as the base.
 *
 * @param <T> the type of the value maintained by this parameter.
 */
public abstract class BasicOneArgParam<T> implements OneArgParam<T> {
  /** the value of the argument to this parameter */
  protected T arg;

  /** the names by which this parameter may be invoked on the command-line */
  protected final Iterable<String> paramNames;

  /**
   * The help text to be displayed for this parameter.
   */
  protected final String helpText;

  /**
   * Construct a parameter.
   * @param names a set of names by which this parameter can be referenced on the command line.
   * @param dflt the default, starting value of the parameter.
   * @param help the help string for this parameter.
   */
  public BasicOneArgParam(Iterable<String> names, T dflt, String help) {
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
    String htxt = helpText;
    String argDesc = null;
    if(htxt.startsWith("<")) {
      int endidx = htxt.indexOf('>',1);
      if(endidx > 1) {
        htxt = helpText.substring(endidx+1).trim();
        argDesc = helpText.substring(1,endidx);
      }
    }
    if(argDesc == null) {
      argDesc = (arg != null) ? arg.getClass().getSimpleName() : "Argument";
    }
    String nameLine = String.format("%s   <%s>", Param.formatNames(paramNames), argDesc);
    Param.formatTypicalHelp(ps, nameLine, htxt);
  }

  /**
   *  A conversion method to get a T from a string.  All subclasses must
   *  define this for the type they handle.
   *
   * @param param the name of the parameter found on the command line.
   *              In some cases it might affect conversion, but it is also good for error messages.
   * @param arg the argument to convert to a type T.
   * @return the converted argument.
   * @throws Exception if there is a problem with the conversion.
   */
  abstract protected T convertArg(String param, String arg) throws Exception;

  /**
   * A validation method which subclasses can override to restrict the valid values of the argument.
   *
   * @param param the name of the given parameter.
   * @param arg the argument to the parameter, as coverted by {@link #convertArg(String, String)}.
   * @return the validated arg, which doesn't have to mach the provided arg.
   * @throws Exception if the provided arg was invalid.
   */
  protected T validate(String param, T arg) throws Exception {
    return arg;  // default leaves arg unchanged
  }

  @Override
  public void process(String param, String argument) throws ArgParserException {
    try {
      this.arg = validate(param, convertArg(param, argument));
    } catch(ArgParserException ape) {
      // just re-throw if they gave us an ArgParserException already
      throw ape;
    } catch(Exception e) {
      throw new ArgParserException(
        String.format("Argument for <%s> is not valid!", param),
        e);
    }
  }
}
