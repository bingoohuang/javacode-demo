package org.n3r.sandbox.regex;



import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.GrokException;
import oi.thekraken.grok.api.Match;
import org.aicer.grok.dictionary.GrokDictionary;
import org.junit.Test;

import java.util.Map;

public class GrokTest {
    @Test
    public void test1() throws GrokException {
        // Get an instance of grok
        Grok grok = Grok.EMPTY;

        // add a pattern to grok
        grok.addPattern("foo", "\\w+");

        // compile and add semantic
        grok.compile("%{foo:bar}");

        // Match with some log
        Match m = grok.match("hello");
        m.captures();

        // Print
        System.out.println(m.toMap());
        // Then the output should be
        // {bar=hello}
    }

    private static final void displayResults(final Map<String, String> results) {
        if (results != null) {
            for (Map.Entry<String, String> entry : results.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
        }
    }

    @Test
    public void test2() {

        final String rawDataLine1 = "1234567 - israel.ekpo@massivelogdata.net cc55ZZ35 1789 Hello Grok";
        final String rawDataLine2 = "98AA541 - israel-ekpo@israelekpo.com mmddgg22 8800 Hello Grok";
        final String rawDataLine3 = "55BB778 - ekpo.israel@example.net secret123 4439 Valid Data Stream";

        final String expression = "%{EMAIL:username} %{USERNAME:password} %{INT:yearOfBirth}";

        final GrokDictionary dictionary = new GrokDictionary();

        // Load the built-in dictionaries
        dictionary.addBuiltInDictionaries();

        // Resolve all expressions loaded
        dictionary.bind();

        // Take a look at how many expressions have been loaded
        System.out.println("Dictionary Size: " + dictionary.getDictionarySize());

        org.aicer.grok.util.Grok compiledPattern = dictionary.compileExpression(expression);

        displayResults(compiledPattern.extractNamedGroups(rawDataLine1));
        displayResults(compiledPattern.extractNamedGroups(rawDataLine2));
        displayResults(compiledPattern.extractNamedGroups(rawDataLine3));
    }
}
