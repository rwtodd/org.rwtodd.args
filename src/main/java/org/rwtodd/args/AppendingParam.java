package org.rwtodd.args;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppendingParam<T> implements OneArgParam<List<T>> {
    protected OneArgParam<T> delegate;
    protected List<T> arg;

    /**
     * Construct a parameter.
     *
     * @param delegate a OneArgParam that we will delegate to for names and help strings and parsing
     * @param dflt  the default, starting value of the parameter (any provided params will be appended it this).
     */
    public AppendingParam(OneArgParam<T> delegate, List<T> dflt) {
        this.delegate = delegate;
        arg = dflt;
    }

    /**
     * Construct a parameter that defaults to the empty list.
     *
     * @param delegate a OneArgParam that we will delegate to for names and help strings and parsing
     */
    public AppendingParam(OneArgParam<T> delegate) {
        this(delegate, new ArrayList<>());
    }

    @Override
    public void process(String param, String argument) throws ArgParserException {
        delegate.process(param, argument);
        arg.add(delegate.getValue());
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
        for(var k: tempMap.keySet()) {
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

    /**
     * gets the current value of the parameter
     */
    @Override
    public List<T> getValue() {
        return arg;
    }
}
