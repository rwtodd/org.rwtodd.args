package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

/**
 * Base class for command-line parameters that take values.
 * 
 * @author rwtodd
 * @param <T> the type of the value accumulated by the parameter
 */
public sealed interface Param permits OneArgParam, NoArgParam {
    /** Add the parameter's names to a Map<String,Param>
     * 
     */
    void addToMap(Map<String,Param> map);

    
    /** adds help for this parameter to the given stream.
     * 
     * @param ps the stream to use
     */
    void addHelp(PrintStream ps); 
}
