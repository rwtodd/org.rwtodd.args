package org.rwtodd.args;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Parses command-line arguments against given Param<T>'s.
 *
 * @author rwtodd
 */
public class Parser {
    /* a helper class to iterate through the arguments and track our
      verbatim status */
    private static final class ArgumentIterator {
       private boolean verbatim;
       private final String[] argList;
       private int argIdx; // index into argList

       ArgumentIterator(String[] args) {
          argList = args;
          argIdx = -1;
          verbatim = false;
       } 

       boolean isVerbatim() { return verbatim; }
       boolean advance() {
         if(++argIdx < argList.length) {
           if(!verbatim && argList[argIdx].equals("--")) {
             verbatim = true;
             return advance();
           } else {
             return true;
           }
         } else {
           // no more input!
           return false;
         } 
       }

       // beware! no checking for -1 here... it's an internal class so hardly necessary. just use it correctly!
       String getCurrent() { return argList[argIdx]; }

       boolean isNotDashed() {
         final String current = argList[argIdx];
         return verbatim || !current.startsWith("-") || current.length() == 1;
       }

       boolean isDoubleDash() {
         final String current = argList[argIdx];
         return !verbatim && current.startsWith("--");
       }

       boolean isSingleDash() {
         final String current = argList[argIdx];
         return !verbatim && current.startsWith("-");
       }
    }

    private final Map<String, Param> parameterMap;

    public Parser(Param... ps) {
        parameterMap = new HashMap<>();
        for (var p : ps) {  p.addToMap(parameterMap); }
    }

    /**
     * parses the given args, and returns any elements that don't appear to be
     * params in an array.
     *
     * @param args the command-line arguments
     * @return any non-param strings from the input
     * @throws org.rwtodd.args.ArgParserException when a problem is
     *    encountered
     */
    public List<String> parse(String... args) throws ArgParserException {
        var iter = new ArgumentIterator(args);
        var remainingArgs = new ArrayList<String>();
          
        while (iter.advance()) {
          if(iter.isDoubleDash()) {
            final String param = iter.getCurrent().substring(2);
            final int eqIdx = param.indexOf("=");
            if(eqIdx >= 0) {
              runParamWithArg(param.substring(0,eqIdx), param.substring(eqIdx+1));
            } else {
              runParam(param, iter);
            }
          } else if(iter.isSingleDash()) {
            // there can be multiple params run together (e.g. -cvf), so
            // run all the params, forcing no arguments on all except the last one
            final String params = iter.getCurrent().substring(1);
            for(int i = 0; i < params.length() - 1; ++i) {
              runParamWithoutArg(params.substring(i,i+1));
            }
            runParam(params.substring(params.length() - 1), iter);
          } else {
            remainingArgs.add(iter.getCurrent());
          }
        }
        return remainingArgs;
    }

    private void runParamWithArg(String param, String arg) throws ArgParserException {
      Param p = parameterMap.get(param);
      if(p instanceof OneArgParam oap) {
        oap.process(param,arg);
      } else if(p instanceof NoArgParam nap) {
        throw new ArgParserException(String.format("Parameter <%s> does not take arguments!",
                                                   param));
      } else {
        throw new ArgParserException(String.format("Parameter <%s> not found!", param));
      }
    }

    private void runParamWithoutArg(String param) throws ArgParserException {
      Param p = parameterMap.get(param);
      if(p instanceof NoArgParam nap) {
        nap.process(param);
      } else if(p instanceof OneArgParam nap) {
        throw new ArgParserException(String.format("Parameter <%s> needs an argument!",
                                                   param));
      } else {
        throw new ArgParserException(String.format("Parameter <%s> not found!", param));
      }
    }

    private void runParam(String param, ArgumentIterator iter) throws ArgParserException {
      Param p = parameterMap.get(param);
      if(p instanceof OneArgParam oap) {
        if(iter.advance() && iter.isNotDashed()) {
          oap.process(param, iter.getCurrent());        
        } else {
          throw new ArgParserException(String.format("Parameter <%s> was not given an argument!",
                                                     param));
        }
      } else if(p instanceof NoArgParam nap) {
        nap.process(param);
      } else {
        throw new ArgParserException(String.format("Parameter <%s> not found!", param));
      }
    }
}
