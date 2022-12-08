package org.rwtodd.args;

import java.util.stream.IntStream;

/**
 * <p>A parameter type that accepts lists of integers.  The list can take comma-separated
 * integers, as well as dots-separated ranges, and combinations of these.  They will
 * be combined into an {@link IntStream}.  The ends of the ranges are inclusive.  Example:</p>
 *
 * <pre>
 * {@code
 *   1,5..8,13  ==> 1,5,6,7,8,13
 * }
 * </pre>
 *
 * @see AppendingParam
 * @author Richard Todd
 */
public class IntListParam extends BasicOneArgParam<IntStream> {

    /**
     * Constructs the parameter.
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param dflt the default, starting value of the parameter.
     * @param help the help string for this parameter.
     */
    public IntListParam(Iterable<String> names, IntStream dflt, String help) {
        super(names, dflt, help);
    }

    /**
     * Constructs the parameter.
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param help the help string for this parameter.
     */
    public IntListParam(Iterable<String> names, String help) {
        this(names, IntStream.empty(), help);
    }

    @Override
    protected IntStream convertArg(String param, String arg) throws Exception {
        int start = 0, end = 0;
        IntStream answer = IntStream.empty();
        IntStream.Builder bldr = null;

        while(start < arg.length()) {
            end = arg.indexOf(',', start);
            if(end == -1) { end = arg.length(); }
            // ok now [start,end) is an entry... does it have a ".." in it?
            final String entry = arg.substring(start,end);
            int dots = entry.indexOf("..");
            if(dots != -1) {
                // put any entries we had collected into the answer
                if(bldr != null) {
                    answer = IntStream.concat(answer,bldr.build());
                    bldr = null;
                }
                // parse a range
                int left = Integer.parseInt(entry.substring(0,dots));
                int right = Integer.parseInt(entry.substring(dots+2));
                answer = IntStream.concat(answer, IntStream.rangeClosed(left,right));
            } else {
                // it's just one number
                if (bldr == null) bldr = IntStream.builder();
                bldr.add(Integer.parseInt(entry));
            }
            start = end + 1;
        }

        // if we were working on entries, don't forget them...
        if(bldr != null) answer = IntStream.concat(answer,bldr.build());
        return answer;
    }
}
