package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class ClampedParam<T extends Comparable<T>> implements OneArgParam<T> {
    private final T min, max;

    private final OneArgParam<T> delegate;

    public ClampedParam(OneArgParam<T> delegate, T min, T max) {
        this.delegate = delegate;
        this.min = min;
        this.max = max;
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
    }

    /**
     * gets the current value of the parameter
     */
    @Override
    public T getValue() {
        T v = delegate.getValue();
        if(v.compareTo(min) < 0) { v = min; }
        else if(v.compareTo(max) > 0) { v = max; }
        return v;
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
