package org.rwtodd.args;

/**
 * IntParam is a Param<T> that accepts integer arguments.
 * @author rwtodd
 */
public class IntParam extends Param<Integer> {

    public IntParam(String longname, char shortname, String argname, String help, Integer deflt) {
        super(longname, shortname, argname, help, deflt);
    }
    
    public IntParam(String longname, char shortname, String argname, String help) {
        this(longname, shortname, argname, help, Integer.MIN_VALUE);
    }
    
    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        try {
            this.arg = Integer.valueOf(value);            
        } catch(NumberFormatException nfe) {
            throw new IllegalArgumentException(
                    String.format("<%s> is not an integer!", value),
                    nfe);
        }
    }

}
