/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rwtodd.args;

import java.io.PrintStream;

/**
 * An exceptional situation from the command-line parser. This also gets thrown
 * when the user invokes a "help" option, and it has the ability to produce
 * portions of a usage string.
 *
 * @author rwtodd
 */
public class CommandLineException extends Exception {
    final Parser parser;
    final boolean helpRequested;

    /** Construct a CommandLineException.
     * 
     * @param helpRequested was help requested?
     * @param desc what was the error?
     * @param cause what exception caused this one?
     */
    public CommandLineException(Parser p, boolean helpRequested, String desc, Throwable cause) {
        super(desc, cause);
        parser = p;
        this.helpRequested = helpRequested;
    }

    /**
     * Construct a CommandLineException.
     * @param helpRequested was help requested?
     * @param desc what was the error?
     */
    public CommandLineException(Parser p, boolean helpRequested, String desc) {
        super(desc);
        parser = p;
        this.helpRequested = helpRequested;
    }
    
    /** is the exception because the user asked for help text?
     * 
     * @return true for yes, false for no
     */
    public boolean helpWasRequested() { return helpRequested; }
    
    /** adds parameter help text to the given stream.
     * 
     * @param ps the PrintStream to use
     */
    public void addHelpText(PrintStream ps) {
        parser.allParams().forEach(p -> p.addHelp(ps));
    }
}
