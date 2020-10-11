package org.rwtodd.args;

/**
 * Represents a boolean flag, which takes no arguments, turns true when
 * provided.
 *
 * @author rwtodd
 */
public class FlagParam extends Param<Boolean> {

    public FlagParam(String longname, char shortname, String help) {
        super(longname, shortname, null, help);
    }

    @Override
    protected boolean needsArg() {
        return false;
    }

    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        this.arg = Boolean.TRUE;
    }
}
