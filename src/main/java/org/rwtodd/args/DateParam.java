package org.rwtodd.args;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

public class DateParam extends BasicOneArgParam<LocalDate> {

    public DateParam(Collection<String> names, LocalDate dflt, String help) {
        super(names, dflt, help);
    }
    public DateParam(Collection<String> names, String help) {
        this(names, LocalDate.now(), help);
    }

    /**
     * A conversion method to get a LocalDate from a string.
     *
     * @param param the name of the parameter found on the command line.
     *              In some cases it might affect conversion, but it is also good for error messages.
     * @param arg   the argument to convert to a type T.
     * @return the converted argument.
     * @throws Exception if there is a problem with the conversion.
     */
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
