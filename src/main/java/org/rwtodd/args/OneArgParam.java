package org.rwtodd.args;

public non-sealed interface OneArgParam extends Param {
  void process(String param, String argument) throws ArgParserException;
}
