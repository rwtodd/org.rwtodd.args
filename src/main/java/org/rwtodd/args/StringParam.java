/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rwtodd.args;

/**
 *
 * @author rwtodd
 */
public class StringParam extends Param<String> {

    public StringParam(String longname, char shortname, String argname, String help, String deflt) {
        super(longname, shortname, argname, help, deflt);
    }
    
    public StringParam(String longname, char shortname, String argname, String help) {
        this(longname, shortname, argname, help, "");
    }

    @Override
    protected void acceptArg(String value) throws IllegalArgumentException {
        this.arg = value;
    }
    
}
