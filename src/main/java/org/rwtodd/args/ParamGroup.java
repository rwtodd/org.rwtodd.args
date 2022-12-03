package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

/**
 * This is a named group of Params for the purpose of organizing help text.
 * It is not a param itself, and doesn't add itself to the {@code Parser}'s map.
 */
public class ParamGroup implements DecorativeParam {
    private final String groupName;
    private final Param[] subParams;

    /**
     * Creates a ParamGroup.
     * @param name the name of the group, displayed above the contained params.
     * @param params the parameters in the group.
     */
    public ParamGroup(String name, Param... params) {
        groupName = name;
        subParams = params;
    }

    /**
     * Add the parameter's names to a {@code Map<String,Param>}.
     *
     * @param map the {@code Map} to which our names should be added.
     */
    @Override
    public void addToMap(Map<String, Param> map) {
        for(final var p: subParams) {
            p.addToMap(map);
        }
    }

    /**
     * adds help for this parameter to the given stream.
     *
     * @param ps the stream to use
     */
    @Override
    public void addHelp(PrintStream ps) {
        ps.print("## ");
        ps.println(groupName);
        for(final var p: subParams) {
            p.addHelp(ps);
        }
        ps.println();
    }
}
