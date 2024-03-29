package org.rwtodd.args;

/**
 * An exceptional situation from the command-line parser.
 *
 * @author Richard Todd
 */
public class ArgParserException extends Exception {
    /** Construct an ArgParserException.
     * 
     * @param desc what was the error?
     * @param cause what exception caused this one?
     */
    public ArgParserException(String desc, Throwable cause) {
        super(desc, cause);
    }

    /**
     * Construct an ArgParserException.
     *
     * @param desc what was the error?
     */
    public ArgParserException(String desc) {
        super(desc);
    }
}
