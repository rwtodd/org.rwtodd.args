package org.rwtodd.args;

public class ClampedIntParam extends IntParam {
    private final int min, max;

    public ClampedIntParam(Iterable<String> names, Integer dflt, int min, int max, String help) {
        super(names, dflt, help);
        this.min = min;
        this.max = max;
    }

    @Override
    protected Integer validate(String param, Integer arg) throws Exception {
        if(arg < min) return min;
        if(arg > max) return max;
        return arg;
    }
}
