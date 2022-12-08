package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A parameter that restricts the values appearing on the command-line to a given range.
 * Any values out of range will result in an exception.  It wraps
 * an existing {@link OneArgParam}, and uses the wrapped param for names, defaults, and processing.</p>
 * <p>Only the {@code BoundedParam} should be added to the {@link Parser}; the wrapped delegate
 * should usually only be referenced by {@code BoundedParam}</p>
 * <p>Example:</p><pre><code>
 *  var ap = new BoundedParam(new IntParam(List.of("port"),6000,"port # (6000-8000)"),
 *               6000,
 *               8000);
 *  var p = new Parser(ap);
 * </code></pre>
 * <p>...which can parse ports and throw an exception if they are out of the 6000-8000 range.</p>
 * @param <T> the type of value maintained by the param.
 * @see ClampedParam
 * @author Richard Todd
 */
public class BoundedParam<T extends Comparable<? super T>> implements OneArgParam<T> {
    /**
     * The type of bounds on the parameter.
     */
    public enum BoundType {
        /**
         * The bounds include both the minimum and maximum value. {@code [min,max]}
         */
        Inclusive,
        /**
         * The bounds include the minimum, but exclude the maximum value. {@code [min,max)}
         */
        Exclusive
    }

    private final T min, max;
    private final BoundType boundtype;

    private final OneArgParam<T> delegate;

    /**
     * Construct the parameter.
     *
     * @param delegate  a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     * @param min  the minimum allowed value
     * @param max the maximum allowed value
     * @param bt the type of bounds represented by the min and max
     */
    public BoundedParam(OneArgParam<T> delegate, T min, T max, BoundType bt) {
        this.delegate = delegate;
        this.min = min;
        this.max = max;
        this.boundtype = bt;
    }

    /**
     * Construct the parameter.  The bounds are set to {@code BoundType.Inclusive}.
     *
     * @param delegate  a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     * @param min  the minimum allowed value
     * @param max the maximum allowed value
     */
    public BoundedParam(OneArgParam<T> delegate, T min, T max) {
        this(delegate, min, max, BoundType.Inclusive);
    }

    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param,argument);
        final T arg = delegate.getValue();
        if(arg.compareTo(min) < 0 ||
                arg.compareTo(max) > 0 ||
                (boundtype == BoundType.Exclusive && arg.compareTo(max) == 0))
            throw new ArgParserException(String.format("Parameter <%s> must be between %s and %s!", param, min.toString(), max.toString()));
    }

    @Override
    public T getValue() {
        return delegate.getValue();
    }

    @Override
    public void addToMap(Map<String, Param> map) {
        final var tempMap = new HashMap<String,Param>();
        delegate.addToMap(tempMap);
        for(final var k: tempMap.keySet()) {
            map.put(k, this);
        }
    }

    @Override
    public void addHelp(PrintStream ps) {
        delegate.addHelp(ps);
    }

}
