package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class BoundedParam<T extends Comparable<T>> implements OneArgParam<T> {
    public enum BoundType { Inclusive, Exclusive }
    private final T min, max;
    private final BoundType boundtype;

    private final OneArgParam<T> delegate;

    public BoundedParam(OneArgParam<T> delegate, T min, T max, BoundType bt) {
        this.delegate = delegate;
        this.min = min;
        this.max = max;
        this.boundtype = bt;
    }
    public BoundedParam(OneArgParam<T> delegate, T min, T max) {
        this(delegate, min, max, BoundType.Inclusive);
    }

    /**
     * Process an parameter with its argument.
     *
     * @param param    the name of the parameter, as it was found in the command-line.
     * @param argument the given argument to the parameter.
     * @throws ArgParserException if there is a problem parsing the argument.
     */
    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param,argument);
        final T arg = delegate.getValue();
        if(arg.compareTo(min) < 0 ||
                arg.compareTo(max) > 0 ||
                (boundtype == BoundType.Exclusive && arg.compareTo(max) == 0))
            throw new ArgParserException(String.format("Parameter <%s> must be between %s and %s!", param, min.toString(), max.toString()));
    }

    /**
     * gets the current value of the parameter
     */
    @Override
    public T getValue() {
        return delegate.getValue();
    }

    /**
     * Add the parameter's names to a {@code Map<String,Param>}.
     *
     * @param map the {@code Map} to which our names should be added.
     */
    @Override
    public void addToMap(Map<String, Param> map) {
        final var tempMap = new HashMap<String,Param>();
        delegate.addToMap(tempMap);
        for(final var k: tempMap.keySet()) {
            map.put(k, this);
        }
    }

    /**
     * adds help for this parameter to the given stream.
     *
     * @param ps the stream to use
     */
    @Override
    public void addHelp(PrintStream ps) {
        delegate.addHelp(ps);
    }

}
