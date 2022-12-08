package org.rwtodd.args;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A param that takes an existing file on the filesystem.
 *
 * @author Richard Todd
 */
public class ExistingFileParam extends BasicOneArgParam<Path> {

    /**
     * Constructs the parameter.
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param dflt the default, starting value of the parameter.
     * @param help the help string for this parameter.
     */
    public ExistingFileParam(Iterable<String> names, Path dflt, String help) {
        super(names, dflt, help);
    }

    /**
     * Constructs the parameter.
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param help the help string for this parameter.
     */
    public ExistingFileParam(Iterable<String> names, String help) {
        this(names, null, help);
    }
    
    @Override
    protected Path convertArg(String param, String argument) throws ArgParserException {
        Path p = Path.of(argument);
        try {
          if(!Files.isRegularFile(p))
            throw new ArgParserException(String.format("Argument for <%s> is not an existing file!",param));
        } catch(SecurityException se) {
          throw new ArgParserException(String.format("Argument for <%s> caused a security exception!",param), se);
        }
        return p;
    }
}
