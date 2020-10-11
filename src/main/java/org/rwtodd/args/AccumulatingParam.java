package org.rwtodd.args;

/**
 * A Param that counts the number of times it is seen.
 *
 * @author rwtodd
 */
public class AccumulatingParam extends Param<Integer> {

    public AccumulatingParam(String longname, char shortname, String help) {
        super(longname, shortname, help, 0);
    }

    @Override
    protected boolean needsArg() {
        return false;
    }

    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        this.arg += 1;
    }

}
