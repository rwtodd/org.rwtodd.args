
# org.rwtodd.args

A java library for processing command-line arguments.

There are many like it, but this one is mine.

## Features

- Small library
- Extensible to new parameter types
- Toolkit, not a framework
- Parameters can have multiple names (-v / --verbose)
- Single-letter parameters can be combined (-tvf == -t -v -f)
- Long params can be connected with their arg or separate (--unit=5 == --unit 5)
- Rudimentary help text output support, with optional named parameter groups
- Support for bounded, clamped, and set-restricted ranges of arguments (especially if they are Comparable)


## on maven central

From gradle you just ask for it:

    implementation 'org.rwtodd:org.rwtodd.args:2.0.0'

