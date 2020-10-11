package org.rwtodd.args;

/**
 * A param to display help text.
 * @author rwtodd
 */
public class HelpParam extends Param<Void> {
    public HelpParam(String longname, char shortname, String help) {
        super(longname, shortname, help);
    }
    
    public HelpParam() {
        this("help",'?',"Gives this help message.");
    }
    
    @Override
    public boolean needsArg() { return false; }
    
    @Override
    public boolean isHelp() { return true; }
    
    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        throw new IllegalArgumentException("Help called");
    }    
}
