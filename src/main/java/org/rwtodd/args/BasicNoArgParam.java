package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

public abstract class BasicNoArgParam<T> implements NoArgParam {
  T arg;
  protected final Collection<String> paramNames;
  protected final String helpText;

  public BasicNoArgParam(Collection<String> names, T dflt, String help) {
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
    Param.formatTypicalHelp(ps, Param.formatNames(paramNames), helpText);
  }
}
