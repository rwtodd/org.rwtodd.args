package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>A parameter that restricts the values appearing on the command-line to a given set of allowed values.
 * Any values out of range will result in a throw exception.  It wraps
 * an existing {@link OneArgParam}, and uses the wrapped param for names, defaults, and processing.</p>
 * <p>Only the {@code SetRestrictedParam} should be added to the {@link Parser}; the wrapped delegate
 * should usually only be referenced by {@code SetRestrictedParam}</p>
 * <p>Example:</p><pre><code>
 *  var srp = new SetRestrictedParam(new IntParam(List.of("count"),1,"count must be 1,3, or 27"),
 *                                   List.of(1,3,27));
 *  var p = new Parser(srp);
 * </code></pre>
 * <p>...which can parse counts and make sure the user only selected either 1, 3, or 27.</p>
 * @param <T> the type of value maintained by the param.
 * @see BoundedParam
 * @author Richard Todd*
 */
public class SetRestrictedParam<T> implements OneArgParam<T> {
    private Set<T> restrictTo;
    private OneArgParam<T> delegate;

    /**
     * Constructs the parameter.
     *
     * @param delegate  a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     * @param restriction the set of allowed values that this parameter can take.
     */
    public SetRestrictedParam(OneArgParam<T> delegate, Iterable<T> restriction) {
        this.delegate = delegate;
        restrictTo = new HashSet<T>();
        for(final T r: restriction) {
            restrictTo.add(r);
        }
    }

    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param,argument);
        if(!restrictTo.contains(delegate.getValue())) {
            throw new ArgParserException(String.format("Argument to <%s> is not in the allowed list!", param));
        }
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
