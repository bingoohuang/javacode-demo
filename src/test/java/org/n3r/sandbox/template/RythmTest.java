package org.n3r.sandbox.template;

import org.junit.Test;
import org.rythmengine.Rythm;
import org.rythmengine.utils.NamedParams;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.rythmengine.Rythm.*;

// some demo from https://github.com/greenlaw110/rythm

public class RythmTest {
    @Test
    public void testSimple() {
        assertTrue(Rythm.engine().isProdMode());

        // Inline template
        assertEquals("Hello world!", render("Hello @who!", "world"));
        assertEquals("Hello world!", substitute("Hello @who!", "world"));
        assertEquals("Hello worldXXX", render("Hello @(who)XXX", "world"));

        // outline template
        assertEquals("<html>\n" +
                "<head>\n" +
                "<title>Hello world from Rythm</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>", render("org/n3r/sandbox/template/RythmHello.html", "World"));

        String expected = "<html>\n" +
                "<head>\n" +
                "<title>Hello world from Rythm</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Greeting World</h1>\n" +
                "</body>\n" +
                "</html>";
        String template = "org/n3r/sandbox/template/RythmGreeting.html";
        assertEquals(expected, render(template, "Greeting", "World"));
        assertEquals(expected, render(template, of("action", "Greeting", "who", "World")));

        // variable placeholder by index
        assertEquals("Hello world war!", render("Hello @1 @2!", "world", "war"));
        assertEquals("Hello worldwar!", render("Hello @(1)@(2)!", "world", "war"));

        // variable placeholder by name
        assertEquals("Hello world", render("Hello @who", of("who", "world")));
        assertEquals("Hello world", render("Hello @who @//comment", of("who", "world")));
        assertEquals("Hello world", render("Hello @who @*comment*@", of("who", "world")));

        NamedParams np = NamedParams.instance;
        assertEquals("Hello world", render("Hello @who", np.from(np.pair("who", "world"))));
    }

    @Test
    public void testTemplateHome() {

    }
}
