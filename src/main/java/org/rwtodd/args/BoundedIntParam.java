package org.rwtodd.args;

public class BoundedIntParam extends IntParam {
    private final int min, max;

    public BoundedIntParam(Iterable<String> names, Integer dflt, int min, int max, String help) {
        super(names, dflt, help);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Integer validate(String param, Integer arg) throws Exception {
        if(arg < min || arg > max)
            throw new ArgParserException(String.format("Parameter <%s> must be between %d and %d!", param, min, max));
        return arg;
    }
}
