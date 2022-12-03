package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

public abstract class BasicNoArgParam<T> implements NoArgParam<T> {
  T arg;
  protected final Iterable<String> paramNames;
  protected final String helpText;

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
