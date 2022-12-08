package org.rwtodd.args;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <p>A parameter that reads a calendar date from the command-line.
 * The following formats are allowed:</p>
 * <ul>
 *     <li>{@code yyyy-mm-dd} for a full date</li>
 *     <li>{@code mm-dd} for a date in the current year</li>
 *     <li>{@code dd} for a day in the current month</li>
 *     <li>the constants: {@code today}, {@code tomorrow}, {@code yesterday}</li>
 *     <li>expressions of the form {@code t+nn} for nn days from now. (e.g., t+5 == five days from now)</li>
 *     <li>expressions of the form {@code t-nn} for nn days ago. (e.g., t-7 == seven days ago)</li>
 * </ul>
 *
 * @author Richard Todd
 */
public class DateParam extends BasicOneArgParam<LocalDate> {

    /**
     * Constructs a parameter.
     *
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param dflt the default, starting value of the parameter.
     * @param help the help string for this parameter.
     */
    public DateParam(Iterable<String> names, LocalDate dflt, String help) {
        super(names, dflt, help);
    }

    /**
     * Constructs a parameter which defaults to today's date if the parameter does not appear on the command-line.
     *
     * @param names a set of names by which this parameter can be referenced on the command line.
     * @param help the help string for this parameter.
     */
    public DateParam(Iterable<String> names, String help) {
        this(names, LocalDate.now(), help);
    }

    @Override
    protected LocalDate convertArg(String param, String arg) throws Exception {
        final var today = LocalDate.now();

        // check for some hard-coded constants
        if(arg.equalsIgnoreCase("today")) {
            return today;
        } else if(arg.equalsIgnoreCase("yesterday")) {
            return today.minusDays(1);
        } else if(arg.equalsIgnoreCase("tomorrow")) {
            return today.plusDays(1);
        }

        try {
            // check for "t+days" and "t-days" for relative dates...
            if (arg.startsWith("t+") || arg.startsWith("t-")) {
                int offset = Integer.parseInt(arg.substring(1));
                return today.plusDays(offset);
            }

            final var nums = Arrays.stream(arg.split("-")).mapToInt(Integer::parseInt).toArray();
            return switch(nums.length) {
                case 3 -> LocalDate.of(nums[0],nums[1],nums[2]);
                case 2 -> LocalDate.of(today.getYear(),nums[0],nums[1]);
                case 1 -> LocalDate.of(today.getYear(),today.getMonthValue(),nums[0]);
                default -> throw new IllegalArgumentException("bad format!");
            };
        } catch(Exception e) {
            throw new ArgParserException(
                    String.format("Argument for <%s> is not in yyyy-mm-dd format, or t+n format, or yesterday/today/tomorrow constants!", param),
                    e);
        }
    }
}
