package org.rwtodd.args;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>A parameter that builds a list of all instances which appear on the command-line. It wraps
 * an existing {@link OneArgParam}, and uses the wrapped param for names and processing.</p>
 * <p>Only the {@code AppendingParam} should be added to the {@link Parser}; the wrapped delegate
 * should usually only be referenced by {@code AppendingParam}</p>
 * <p>Example:</p><pre><code>
 *  var ap = new AppendingParam(new IntParam(List.of("port","p"),"the ports to use"));
 *  var p = new Parser(ap);
 * </code></pre>
 * <p>...which can parse repeated params {@code -p 12 --port=5 -p 108} into the list {@code [12,5,108]}</p>
 * @param <T> the type of the parameter, which it builds into a list
 *
 * @author Richard Todd
 */
public class AppendingParam<T> implements OneArgParam<List<T>> {
    /**
     * a delegate parameter, which is used to get names and to process argument strings.
     */
    protected OneArgParam<T> delegate;

    /**
     * the list this parameter maintains as it processes arguments.
     */
    protected List<T> arg;

    /**
     * Construct a parameter.
     *
     * @param delegate a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     * @param dflt  the default, starting value of the parameter (any provided params will be appended it this).
     */
    public AppendingParam(OneArgParam<T> delegate, List<T> dflt) {
        this.delegate = delegate;
        arg = dflt;
    }

    /**
     * Construct a parameter that defaults to the empty list.
     *
     * @param delegate a {@link OneArgParam} that we will delegate to for names and help strings and parsing
     */
    public AppendingParam(OneArgParam<T> delegate) {
        this(delegate, new ArrayList<>());
    }

    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param, argument);
        arg.add(delegate.getValue());
    }

    @Override
    public void addToMap(Map<String, Param> map) {
        final var tempMap = new HashMap<String,Param>();
        delegate.addToMap(tempMap);
        for(var k: tempMap.keySet()) {
            map.put(k, this);
        }
    }

    @Override
    public void addHelp(PrintStream ps) {
        delegate.addHelp(ps);
    }

    @Override
    public List<T> getValue() {
        return arg;
    }
}
