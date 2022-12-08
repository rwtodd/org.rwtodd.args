package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A parameter that restricts the values appearing on the command-line to a given range.
 * Any values out of range will clamped to the min or max.  It wraps
 * an existing {@link OneArgParam}, and uses the wrapped param for names, defaults, and processing.</p>
 * <p>Only the {@code ClampedParam} should be added to the {@link Parser}; the wrapped delegate
 * should usually only be referenced by {@code ClampedParam}</p>
 * <p>Example:</p><pre><code>
 *  var ap = new ClampedParam(new IntParam(List.of("port"),6000,"port # (6000-8000)"),
 *               6000,
 *               8000);
 *  var p = new Parser(ap);
 * </code></pre>
 * <p>...which can parse ports and set any values below 6000 to 6000, and any values above 8000 to
 * 8000.</p>
 * @param <T> the type of value maintained by the param.
 * @see BoundedParam
 * @author Richard Todd*
 */
public class ClampedParam<T extends Comparable<T>> implements OneArgParam<T> {
    private final T min, max;

    private final OneArgParam<T> delegate;

    /**
     * Construct the parameter.
     *
     * @param delegate  a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     * @param min  the minimum allowed value
     * @param max the maximum allowed value
     */
    public ClampedParam(OneArgParam<T> delegate, T min, T max) {
        this.delegate = delegate;
        this.min = min;
        this.max = max;
    }

    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param,argument);
    }

    @Override
    public T getValue() {
        T v = delegate.getValue();
        if(v.compareTo(min) < 0) { v = min; }
        else if(v.compareTo(max) > 0) { v = max; }
        return v;
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
