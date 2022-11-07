package org.rwtodd.args;

/**
 * An exceptional situation from the command-line parser.
 *
 * @author rwtodd
 */
public class ArgParserException extends Exception {
    /** Construct a ArgParserException.
     * 
     * @param desc what was the error?
     * @param cause what exception caused this one?
     */
    public ArgParserException(String desc, Throwable cause) {
        super(desc, cause);
    }

    /**
     * Construct a ArgParserException.
     * @param desc what was the error?
     */
    public ArgParserException(String desc) {
        super(desc);
    }
}
