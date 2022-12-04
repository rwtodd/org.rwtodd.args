package org.rwtodd.args;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SetRestrictedParam<T> implements OneArgParam<T> {
    private Set<T> restrictTo;
    private OneArgParam<T> delegate;

    public SetRestrictedParam(OneArgParam<T> delegate, Iterable<T> restriction) {
        this.delegate = delegate;
        restrictTo = new HashSet<T>();
        for(final T r: restriction) {
            restrictTo.add(r);
        }
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
        if(!restrictTo.contains(delegate.getValue())) {
            throw new ArgParserException(String.format("Argument to <%s> is not in the allowed list!", param));
        }
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
