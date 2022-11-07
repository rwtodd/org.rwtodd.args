package org.rwtodd.args;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A param that takes expects an existing file on the filesystem
 * @author rwtodd
 */
public class ExistingFileParam extends BasicOneArgParam<Path> {

    public ExistingFileParam(Collection<String> names, Path dflt, String help) {
        super(names, dflt, help);
    }
    
    public ExistingFileParam(Collection<String> names, String help) {
        this(names, null, help);
    }
    
    @Override
    protected Path convertArg(String param, String argument) throws ArgParserException {
        Path p = Path.of(argument);
        if(Files.notExists(p))
            throw new ArgParserException(String.format("Argument for <%s> is not an exisiting file!",param));
        return p;
    }
    
}
