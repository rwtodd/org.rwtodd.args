package org.rwtodd.args;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A param that takes expects an existing file on the filesystem
 * @author rwtodd
 */
public class ExistingFileParam extends Param<Path> {

    public ExistingFileParam(String longname, char shortname, String argname, String desc, Path deflt) {
        super(longname, shortname, argname, desc, deflt);
    }
    
    public ExistingFileParam(String longname, char shortname, String argname, String desc) {
        this(longname,shortname,argname,desc,null);
    }
    
    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        this.arg = Path.of(value);
        if(Files.notExists(this.arg))
            throw new IllegalArgumentException("Not an exisiting file: " +value);
    }
    
}
