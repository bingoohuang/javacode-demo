package org.n3r.sandbox.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class Dom4jTest {
    @Test
    public void testExpandEmptyElements() throws IOException {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");
        Element id = root.addElement("id");
        id.addText("1");

        root.addElement("empty");

        OutputFormat xmlFormat = new OutputFormat();
        // OutputFormat.createPrettyPrint();
        xmlFormat.setSuppressDeclaration(true);
        xmlFormat.setEncoding("UTF-8");
        // If this is true, elements without any child nodes
        // are output as <name></name> instead of <name/>.
        xmlFormat.setExpandEmptyElements(true);


        StringWriter out = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(out, xmlFormat);
        xmlWriter.write(document);
        xmlWriter.close();

        assertEquals("<root><id>1</id><empty></empty></root>", out.toString());
    }
}
