package org.rwtodd.args;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parses command-line arguments against given Param<T>'s.
 *
 * @author rwtodd
 */
public class Parser {

    private final Map<String, Param<?>> params;
    private final Map<Character, Param<?>> shortParams;
    private boolean helpCalled;

    public Parser(Param<?>... ps) {
        helpCalled = false;
        params = new HashMap<>();
        shortParams = new HashMap<>();
        for (var p : ps) {
            params.put(p.getName(), p);
            if (p.getShortName() != ' ') {
                shortParams.put(p.getShortName(), p);
            }
        }
    }

    /**
     * convert a string into one or more String|Exception|Param<?>'s. Since java
     * doesn't do union types without a lot of boilerplate, settle for Object in
     * this internal method.
     *
     * @param arg a command-line argument.
     * @return A stream of items which can be strings or Param<?>'s.
     */
    private Stream<Object> expandArg(String arg) {
        if (arg.startsWith("--")) {
            final var eqidx = arg.indexOf('=');
            if (eqidx == -1) {
                // we have a simple --long-argument
                final var found = params.get(arg.substring(2));
                if (found != null) {
                    return Stream.of(found);
                } else {
                    return Stream.of(new IllegalArgumentException(
                            "Unknown parameter: " + arg
                    ));
                }
            } else {
                // we have a --param=value type of argument
                final var val = arg.substring(eqidx + 1);
                final var found = params.get(arg.substring(2, eqidx));
                if (found != null) {
                    return Stream.of(found, val);
                } else {
                    return Stream.of(new IllegalArgumentException(
                            "Unknown parameter: " + arg.substring(0, eqidx)
                    ));
                }
            }
        } else if (arg.startsWith("-") && (arg.length() > 1)) {
            // we have a short switch, possibly bunched together or grouped
            // with an argument -tvfname == -t -v -f name
            final var plist = new ArrayList<Object>();
            for (int idx = 1; idx < arg.length(); idx++) {
                final var p = shortParams.get(arg.charAt(idx));
                if (p != null) {
                    plist.add(p);
                    if (p.needsArg()) {
                        if(arg.length() > (idx+1))
                            plist.add(arg.substring(idx + 1));
                        break;
                    }
                } else {
                    plist.add(new IllegalArgumentException("Unknown short arg -" + arg.charAt(idx)));
                    break;
                }
            }
            return plist.stream();
        } else {
            return Stream.of(arg);
        }
    }

    /**
     * parses the given args, and returns any elements that don't appear to be
     * params in an array.
     *
     * @param args the command-line arguments
     * @return any non-param strings from the input
     * @throws org.rwtodd.args.CommandLineException when a problem is
     * encountered, or help is requested
     */
    public List<String> parse(String... args) throws CommandLineException {
        var dashdash = Arrays.asList(args).indexOf("--");
        if (dashdash < 0) {
            dashdash = args.length;
        }

        var expandedArgs = Arrays.stream(args, 0, dashdash)
                .flatMap(this::expandArg).iterator();
        var remainingArgs = new ArrayList<String>();

        while (expandedArgs.hasNext()) {
            var arg = expandedArgs.next();
            if (arg instanceof IllegalArgumentException) {
                throw new CommandLineException(this, false, ((IllegalArgumentException) arg).getMessage());
            } else if (arg instanceof Param<?>) {
                final var param = (Param<?>) arg;
                try {
                    if (!param.needsArg()) {
                        param.acceptArg(null);
                    } else {
                        if (!expandedArgs.hasNext()) {
                            throw new CommandLineException(this, false, "No argument given for the <" + param.getName() + "> option!");
                        }
                        final var value = expandedArgs.next();
                        if (value instanceof String) {
                            param.acceptArg((String) value);
                        } else {
                            throw new CommandLineException(this, false, "No argument given for the <" + param.getName() + "> option!");
                        }
                    }
                    
                    // now validate the argument, if needed
                    if(!param.isValidValue()) {
                        throw new CommandLineException(
                                this,
                                false,
                                String.format("Param %s's value (%s) is not valid!",
                                        param.getName(),
                                        param.getValue().toString()));
                    }
                } catch (IllegalArgumentException iae) {
                    throw new CommandLineException(this, param.isHelp(), iae.getMessage());
                }
            } else if (arg instanceof String) {
                remainingArgs.add((String) arg);
            } else {
                throw new RuntimeException("Somehow an argument was neither a param nor a string!");
            }
        }

        if (dashdash < (args.length - 1)) {
            Arrays.stream(args, dashdash + 1, args.length).forEachOrdered(remainingArgs::add);
        }
        return remainingArgs;
    }

    /**
     * Throws an exception which requests the help text be printed.
     *
     * @throws CommandLineException
     */
    public void requestHelp() throws CommandLineException {
        throw new CommandLineException(this, true, "Help Requested");
    }

    /**
     * Returns a list of all params, sorted by long name.
     *
     * @return the list of params.
     */
    List<Param<?>> allParams() {
        return params.values().stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());
    }
}
