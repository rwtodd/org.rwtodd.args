package org.rwtodd.args;

import java.io.PrintStream;

/**
 * Base class for command-line parameters that take values.
 * 
 * @author rwtodd
 * @param <T> the type of the value accumulated by the parameter
 */
public abstract class Param<T> {
    private final String longName;
    private final char shortName;
    private final String helpStr;
    protected T arg;
    
    public Param(String longname, char shortname, String help, T deflt) {
        arg = deflt;
        helpStr = help;
        longName = longname;
        shortName = shortname;
    }
    
    public Param(String longname, char shortname, String help) {
        this(longname, shortname, help, null);
    }
    
    /** get the long name for the parameter.
     * 
     * @return the long name.
     */
    public String getName() { return longName; }
    
    /** get the short name for the parameter.
     * 
     * @return the short name.
     */
    public char getShortName() { return shortName; }
    
    /** does this parameter need an argument?
     * 
     * @return true for yes, false for no. 
     */
    protected boolean needsArg() { return true; }
    
    /** take the given string, (null if needsArgs() is false), and
     *  perform the appropriate action for this parameter type.
     * 
     * @param value The given parameter argument.
     * @throws IllegalArgumentException when the value is not well-formed for this
     * parameter type.
     */
    protected abstract void acceptArg(String value)
            throws IllegalArgumentException;
    
    /** Retrieves the value out of this parameter.
     * 
     * @return the current set value for this parameter.
     */
    public T getValue() { return arg; }
    
    /** Identify this param as the "Help" param.
     * 
     * @return true if it is the "help" param, false otherwise.
     */
    public boolean isHelp() { return false; }
    
    /** adds help for this parameter to the given stream.
     * 
     * @param ps the stream to use
     */
    public void addHelp(PrintStream ps) {
        ps.format("%s\t%s", longName, helpStr);
    }
}
