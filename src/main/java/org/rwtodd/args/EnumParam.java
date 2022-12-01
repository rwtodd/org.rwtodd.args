package org.rwtodd.args;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.PrintStream;

/** A parameter which gives options based on enum values.
 * 
 *  The right way to instantiate it is like this:
 *  {@code new EnumParam<>(MyEnum.class, "help string")}.
 */
public class EnumParam<T extends Enum<T>> implements NoArgParam {
  private final Class<T> clz; // need to hold on to the enum class, silly Java!
  private final String helpStr;
  private T value;
 
  public EnumParam(Class<T> c, T dflt, String help) {
     clz = c;
     value = dflt;
     helpStr = help;
  }

  /** Create the EnumParam without a default (it will be null) */
  public EnumParam(Class<T> c, String help) {
     this(c, null, help);
  }

  /** Add the parameter's names to a {@code Map<String,Param>}.
   * 
   */
  public void addToMap(Map<String,Param> map) {
     for(T econst: clz.getEnumConstants()) {
       map.put(econst.toString(), this);
     }
  }

    
 /** adds help for this parameter to the given stream.
  * 
  * @param ps the stream to use
  */
  public void addHelp(PrintStream ps) {
      List.of(clz.getEnumConstants());
      Param.formatTypicalHelp(ps,
              Param.formatNames(Arrays.stream(clz.getEnumConstants()).map(Enum::toString).toList()),
              helpStr);
  }

  public void process(String param) throws ArgParserException {
    try {
        value = Enum.valueOf(clz, param);
    } catch(Exception e) {
        throw new ArgParserException(String.format("Param <%s> should be a defined constant, but it isn't!", param));
    }
  }
  
  public T getValue() { return value; }
}

