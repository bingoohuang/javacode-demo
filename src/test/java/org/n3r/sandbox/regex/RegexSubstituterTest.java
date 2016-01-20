package org.n3r.sandbox.regex;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.n3r.sandbox.regex.RegexSubstituter.substitute;

public class RegexSubstituterTest {
    @Test
    public void testBasic() {
        RegexSubstituter r4 = new RegexSubstituter("s/foo/bar/g");
        assertEquals("bar and bar again", r4.substitute("foo and foo again"));
    }

    @Test
    public void testSubstitution() {

        assertEquals("sergioAApelaAA", substitute("sergio33pela44", "s/\\d+/AA/gi"));

        assertEquals("sergiopelaAAA", substitute("sergioAAApelaAAA", "s/AAA//"));

        assertEquals("sergiopela", substitute("sergioAAApelaAAA", "s/AAA//g"));

        assertEquals("sergioCCCpelaCCC", substitute("sergioCCCpelaCCC", "s/c//"));

        assertEquals("sergioCCpelaCCC", substitute("sergioCCCpelaCCC", "s/c//i"));

        assertEquals("sergiopela", substitute("sergioCCCpelaCCC", "s/c//gi"));

        assertEquals("sergio/Z/Zpela", substitute("sergioAApela", "s%AA%\\/Z\\/Z%g"));

        assertEquals("sergio\\Z\\Zpela", substitute("sergioAApela", "s%AA%\\\\Z\\\\Z%g"));
    }

    @Test
    public void testMultiCommand() {
        List<RegexSubstituter> regexSubstituters = RegexSubstituter.parse("s/abc/efg/ s%\\d+%**%g");
        assertEquals(2, regexSubstituters.size());

        assertEquals("efgXYZabc", regexSubstituters.get(0).substitute("abcXYZabc"));
        assertEquals("**X**", regexSubstituters.get(1).substitute("1234X456"));
    }
}