package org.rwtodd.args;

import java.io.PrintStream;
import java.util.Map;

/**
 * This is a named group of Params for the purpose of organizing help text.
 * It is not a param itself, and doesn't add itself to the {@link Parser}'s map.
 *
 * @author Richard Todd
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

    @Override
    public void addToMap(Map<String, Param> map) {
        for(final var p: subParams) {
            p.addToMap(map);
        }
    }

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
