package org.rwtodd.args;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A param that takes expects an existing file on the filesystem
 * @author rwtodd
 */
public class ExistingDirectoryParam extends BasicOneArgParam<Path> {

    public ExistingDirectoryParam(Collection<String> names, Path dflt, String help) {
        super(names, dflt, help);
    }
    
    public ExistingDirectoryParam(Collection<String> names, String help) {
        this(names, Path.of("."), help);
    }
    
    @Override
    protected Path convertArg(String param, String argument) throws ArgParserException {
        Path p = Path.of(argument);
        try {
          if(!Files.isDirectory(p))
              throw new ArgParserException(String.format("Argument for <%s> is not an exisiting directory!",param));
        } catch(SecurityException se) {
          throw new ArgParserException(String.format("Argument for <%s> caused a security exception!",param), se);
        }
        return p;
    }
    
}
