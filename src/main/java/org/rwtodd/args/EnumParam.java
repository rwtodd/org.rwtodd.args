package org.rwtodd.args;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.PrintStream;

/** <p>A parameter which gives options based on enum values.</p>
 *  <p>
 *  The right way to instantiate it is like this:
 *  {@code new EnumParam<>(MyEnum.class, "help string");}.
 *  </p>
 *
 * @param <T> the enum which acts as the source for this param.
 * @author Richard Todd
 */
public class EnumParam<T extends Enum<T>> implements NoArgParam<T> {
  private final Class<T> clz; // need to hold on to the enum class, silly Java!
  private final String helpStr;
  private T value;

    /**
     * <p>Constructs a parameter.  The right way to instantiate it is like this:</p>
     * <pre>
     *  {@code new EnumParam<>(MyEnum.class, MyEnum.ENUM_CASE, "help string");}
     * </pre>
     *
     * @param c the class, which must represent an enum.
     * @param dflt the default, starting value of the parameter.
     * @param help the help string for this parameter.
     */
  public EnumParam(Class<T> c, T dflt, String help) {
     clz = c;
     value = dflt;
     helpStr = help;
  }

  /**
   * <p>Constructs a parameter with a null default.  The right way to instantiate it is like this:</p>
   * <pre>
   *  {@code new EnumParam<>(MyEnum.class, "help string");}
   * </pre>
   *
   * @param c the class, which must represent an enum.
   * @param help the help string for this parameter.
   *
   */
  public EnumParam(Class<T> c, String help) {
     this(c, null, help);
  }

  @Override
  public void addToMap(Map<String, Param> map) {
     for(T econst: clz.getEnumConstants()) {
       map.put(econst.toString(), this);
     }
  }

  @Override
  public void addHelp(PrintStream ps) {
      List.of(clz.getEnumConstants());
      Param.formatTypicalHelp(ps,
              Param.formatNames(Arrays.stream(clz.getEnumConstants()).map(Enum::toString).toList()),
              helpStr);
  }

  @Override
  public void process(String param) throws ArgParserException {
    try {
        value = Enum.valueOf(clz, param);
    } catch(Exception e) {
        throw new ArgParserException(String.format("Param <%s> should be a defined constant, but it isn't!", param));
    }
  }
  
  @Override public T getValue() { return value; }
}

