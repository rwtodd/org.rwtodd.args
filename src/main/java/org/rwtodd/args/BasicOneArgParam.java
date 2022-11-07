package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;
import java.util.Collection;

public abstract class BasicOneArgParam<T> implements OneArgParam {
  T arg;
  protected final Collection<String> paramNames;
  protected final String helpText;

  public BasicOneArgParam(Collection<String> names, T dflt, String help) {
    arg = dflt;
    paramNames = names;
    helpText = help; 
  }

  public T getValue() { return arg; }

  @Override
  public void addToMap(Map<String,Param> map) {
    for(String s: paramNames) {
      map.put(s,this);
    }
  }

  @Override
  public void addHelp(PrintStream ps) {
    // TODO: add method body!
  }

  // a conversion method to get a T from a string... throw
  // an exception if it fails
  abstract protected T convertArg(String param, String arg) throws Exception;

  // a validation method which subclasses can override...
  // throw an exception if the arg isn't valid.
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
