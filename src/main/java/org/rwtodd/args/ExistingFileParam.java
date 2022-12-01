package org.rwtodd.args;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A param that takes expects an existing file on the filesystem.
 * @author rwtodd
 */
public class ExistingFileParam extends BasicOneArgParam<Path> {

    public ExistingFileParam(Iterable<String> names, Path dflt, String help) {
        super(names, dflt, help);
    }
    
    public ExistingFileParam(Iterable<String> names, String help) {
        this(names, null, help);
    }
    
    @Override
    protected Path convertArg(String param, String argument) throws ArgParserException {
        Path p = Path.of(argument);
        try {
          if(!Files.isRegularFile(p))
            throw new ArgParserException(String.format("Argument for <%s> is not an exisiting file!",param));
        } catch(SecurityException se) {
          throw new ArgParserException(String.format("Argument for <%s> caused a security exception!",param), se);
        }
        return p;
    }
}
