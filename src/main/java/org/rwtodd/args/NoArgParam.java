package org.rwtodd.args;

public non-sealed interface NoArgParam extends Param {
  void process(String param) throws ArgParserException;
}
