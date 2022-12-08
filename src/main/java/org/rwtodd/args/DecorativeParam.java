package org.rwtodd.args;

/**
 * The base interface for Params that merely decorate the help text output, and
 * do not add themselves to the {@link Parser}'s lookup map.  An error will be
 * thrown if a {@code DecorativeParam} ever gets parsed.
 *
 * @author Richard Todd
 */
public non-sealed interface DecorativeParam extends Param {
    /* empty, just a marker interface */
}
