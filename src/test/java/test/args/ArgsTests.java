/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package test.args;

import java.io.PrintStream;
import org.rwtodd.args.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArgsTests {

    @Test
    void testAccumulator() {
        try {
            final var verbose = new AccumulatingParam("verbose", 'v', "Be more verbose (can repeat this arg)");
            final var p = new Parser(verbose, new HelpParam());
            var extras = p.parse("-vvvv", "-v");
            assertTrue(extras.isEmpty());
            assertEquals(5, verbose.getValue());
        } catch (CommandLineException cle) {
            fail("Exception thrown!", cle);
        }
    }

    @Test
    void testIntParam() {
        try {
            final var ip = new IntParam("count", ' ', "n", "The count of things.");
            final var p = new Parser(ip);
            var extras = p.parse("file", "file");
            assertEquals(2, extras.size());
            assertEquals(Integer.MIN_VALUE, ip.getValue());
            extras = p.parse("--count=21");
            assertEquals(0, extras.size());
            assertEquals(21, ip.getValue());
            extras = p.parse("--count", "486");
            assertEquals(0, extras.size());
            assertEquals(486, ip.getValue());
        } catch (CommandLineException cle) {
            fail("Exception thrown!", cle);
        }
    }

    @Test
    void testStringParam() {
        try {
            final var sp = new StringParam("name", ' ', "name", "The name you want.");
            final var p = new Parser(sp);
            var extras = p.parse("file", "file");
            assertEquals(2, extras.size());
            assertEquals("", sp.getValue());

            extras = p.parse("--name=wally");
            assertEquals(0, extras.size());
            assertEquals("wally", sp.getValue());

            extras = p.parse("--name=", "file");
            assertEquals(1, extras.size());
            assertEquals("", sp.getValue());

            extras = p.parse("--name", "larry");
            assertEquals(0, extras.size());
            assertEquals("larry", sp.getValue());
        } catch (CommandLineException cle) {
            fail("Exception thrown!", cle);
        }
    }

    @Test
    void testHelpRequest() {
        try {
            final var p = new Parser(
                    new IntParam("orders", 'o', "count", "How many orders to send.", 5),
                    new HelpParam());
            var extras = p.parse("--help");
            fail("Exception wasn't thrown!");
        } catch (CommandLineException cle) {
            assertTrue(cle.helpWasRequested());
            var backing = new java.io.ByteArrayOutputStream();
            var ps = new PrintStream(backing);
            cle.addHelpText(ps);
            ps.flush();
            assertEquals(
                    """
                    --help|-?
                        Gives this help message.
                    --orders|-o  <count>
                        How many orders to send.
                    """,
                    backing.toString(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @Test
    void testShortExpansion() {
        // make sure that an argument to -o here shows up as missing
        try {
            final var p = new Parser(
                    new IntParam("orders", 'o', "count", "How many orders to send.", 5),
                    new HelpParam());
            var extras = p.parse("-o");
            fail("Exception not thrown!");
        } catch (CommandLineException cle) {
            /* nothing */
        }
        
        // make sure we can get the argument when appended to the param, and when not
        try {
            final var ip = new IntParam("orders", 'o', "count", "How many orders to send.", 5); 
            final var p = new Parser(
                    ip,
                    new HelpParam());
            var extras = p.parse("-o2");
            assertEquals(2, ip.getValue());
            
            extras = p.parse("-o323");
            assertEquals(323, ip.getValue());

            extras = p.parse("-o","512");
            assertEquals(512, ip.getValue());            
        } catch (CommandLineException cle) {
            fail("Exception thrown!", cle);
        }
        
    }

    @Test
    void testFlagArgs() {
        // make sure we can get the argument when appended to the param, and when not
        try {
            final var fp = new FlagParam("orders", 'o', "Send orders"); 
            final var p = new Parser(
                    fp,
                    new HelpParam());
            var extras = p.parse("hi");
            assertFalse(fp.getValue());
            
            extras = p.parse("hi", "--orders");
            assertTrue(fp.getValue());
        } catch (CommandLineException cle) {
            fail("Exception thrown!", cle);
        }
        
    }
}
